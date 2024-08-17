package io.dpopkov.knowthenixkbd.repo.inmemory

import com.benasher44.uuid.uuid4
import io.dpopkov.knowthenixkbd.common.models.KnthTranslation
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationId
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationLock
import io.dpopkov.knowthenixkbd.common.models.KnthUserId
import io.dpopkov.knowthenixkbd.common.repo.*
import io.dpopkov.knowthenixkbd.common.repo.exceptions.RepoEmptyLockException
import io.dpopkov.knowthenixkbd.repo.common.IRepoTranslationInitializable
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/**
 * Репозиторий на основе in-memory cache, предназначен для тестирования и интеграции со сторонними сервисами.
 */
class TranslationRepoInMemory(
    /** Time to live - время жизни объектов после последней записи в базу. */
    ttl: Duration = 2.minutes,
    /**
     * Поставщик uuid для возможности мокирования идентификаторов, например задания целевого uuid в тестах.
     * По умолчанию генерируется случайный идентификатор.
     */
    val randomUuid: () -> String = { uuid4().toString() }
) : TranslationRepoBase(), IRepoTranslation, IRepoTranslationInitializable {

    /** Mutex для организации синхронного доступа к cache из разных потоков */
    private val mutex = Mutex()
    private val cache = Cache.Builder<String, TranslationEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(translations: Collection<KnthTranslation>): Collection<KnthTranslation> {
        return translations.map { tr ->
            val entity = TranslationEntity(tr)
            requireNotNull(entity.id)
            cache.put(entity.id, entity)
            tr
        }
    }

    override suspend fun createTranslation(req: DbTranslationRequest): IDbTranslationResponse {
        return tryTranslationMethod {
            val key = randomUuid()
            val tr = req.translation.copy(
                id = KnthTranslationId(key),
                lock = KnthTranslationLock(randomUuid()),
            )
            val entity = TranslationEntity(tr)
            mutex.withLock {
                cache.put(key, entity)
            }
            DbTranslationResponseOk(tr)
        }
    }

    override suspend fun readTranslation(req: DbTranslationIdRequest): IDbTranslationResponse {
        return tryTranslationMethod {
            val key = req.id.takeIf {
                it != KnthTranslationId.NONE
            }?.asString() ?: return@tryTranslationMethod errorEmptyId
            mutex.withLock {
                cache.get(key)
                    ?.let {
                        DbTranslationResponseOk(it.toInternal())
                    } ?: errorNotFound(req.id)
            }
        }
    }

    override suspend fun updateTranslation(req: DbTranslationRequest): IDbTranslationResponse {
        return tryTranslationMethod {
            val reqTr = req.translation
            val id = reqTr.id.takeIf { it != KnthTranslationId.NONE }
                ?: return@tryTranslationMethod errorEmptyId
            val key = id.asString()
            val reqLock = reqTr.lock.takeIf { it.isNotNone() }
                ?: return@tryTranslationMethod errorEmptyLock(id)

            mutex.withLock {
                val oldTr = cache.get(key)?.toInternal()
                when {
                    oldTr == null -> errorNotFound(id)
                    oldTr.lock.isNone() -> errorDb(RepoEmptyLockException(id))
                    oldTr.lock != reqLock -> errorRepoConcurrency(
                        oldTranslation = oldTr,
                        expectedLock = reqLock,
                    )

                    else -> {
                        val newLockForUpdatedEntity = KnthTranslationLock(randomUuid())
                        val newTr = reqTr.copy(lock = newLockForUpdatedEntity)
                        val entity = TranslationEntity(newTr)
                        cache.put(key, entity)
                        DbTranslationResponseOk(newTr)
                    }
                }
            }
        }
    }

    override suspend fun deleteTranslation(req: DbTranslationIdRequest): IDbTranslationResponse {
        return tryTranslationMethod {
            val id = req.id.takeIf { it != KnthTranslationId.NONE }
                ?: return@tryTranslationMethod errorEmptyId
            val key = id.asString()
            val reqLock = req.lock.takeIf { it.isNotNone() }
                ?: return@tryTranslationMethod errorEmptyLock(id)

            mutex.withLock {
                val oldTr = cache.get(key)?.toInternal()
                when {
                    oldTr == null -> errorNotFound(id)
                    oldTr.lock.isNone() -> errorDb(RepoEmptyLockException(id))
                    oldTr.lock != reqLock -> errorRepoConcurrency(
                        oldTranslation = oldTr,
                        expectedLock = reqLock,
                    )
                    else -> {
                        cache.invalidate(key)
                        DbTranslationResponseOk(oldTr)
                    }
                }
            }
        }
    }

    override suspend fun searchTranslation(req: DbTranslationFilterRequest): IDbTranslationsResponse {
        return tryTranslationsMethod {
            val result = cache.asMap().asSequence()
                .filter { entry ->
                    val rqOwnerId = req.ownerId.takeIf { it != KnthUserId.NONE }
                    when {
                        rqOwnerId != null -> rqOwnerId.asString() == entry.value.ownerId
                        else -> true
                    }
                }
                .filter { entry ->
                    val reqContentFilter = req.contentFilter.takeIf { it.isNotBlank() }
                    when {
                        reqContentFilter != null -> entry.value.content?.contains(reqContentFilter) ?: false
                        else -> true
                    }
                }
                .map { it.value.toInternal() }
                .toList()
            DbTranslationsResponseOk(result)
        }
    }
}
