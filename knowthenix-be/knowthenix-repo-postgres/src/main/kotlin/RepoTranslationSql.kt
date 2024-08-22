package io.dpopkov.knowthenixkbd.repo.postgresql

import com.benasher44.uuid.uuid4
import io.dpopkov.knowthenixkbd.common.helpers.asKnthError
import io.dpopkov.knowthenixkbd.common.models.KnthTranslation
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationId
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationLock
import io.dpopkov.knowthenixkbd.common.models.KnthUserId
import io.dpopkov.knowthenixkbd.common.repo.*
import io.dpopkov.knowthenixkbd.repo.common.IRepoTranslationInitializable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class RepoTranslationSql(
    properties: SqlProperties,
    private val randomUuid: () -> String = { uuid4().toString() }
) : IRepoTranslation, IRepoTranslationInitializable {
    private val translationTable = TranslationTable(
        tableName = "${properties.schema}.${properties.table}"
    )
    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val conn by lazy {
        println(">>>>>>>>>>>>>>>>>>>>>> properties.url=${properties.url}")
        println(">>>>>>>>>>>>>>>>>>>>>> driver=${driver}")
        Database.connect(
            properties.url, driver, properties.user, properties.password
        )
    }

    private fun saveObj(translation: KnthTranslation): KnthTranslation {
        return transaction(conn) {
            val res: List<KnthTranslation>? = translationTable
                .insert {
                    to(it, translation, randomUuid)
                }
                .resultedValues
                ?.map {
                    translationTable.from(it)
                }
            res?.first() ?: throw RuntimeException("DB error: insert statement returned empty result")
        }
    }

    private suspend inline fun <T> transactionWrapper(
        crossinline mainBlock: () -> T,
        crossinline exceptionHandler: (Exception) -> T
    ): T =
        withContext(Dispatchers.IO) {
            try {
                transaction(conn) {
                    mainBlock()
                }
            } catch (e: Exception) {
                exceptionHandler(e)
            }
        }

    private suspend inline fun transactionWrapper(crossinline block: () -> IDbTranslationResponse) =
        transactionWrapper(block) {
            DbTranslationResponseErr(it.asKnthError())
        }

    override fun save(translations: Collection<KnthTranslation>): Collection<KnthTranslation> =
        translations.map {
            saveObj(it)
        }

    override suspend fun createTranslation(req: DbTranslationRequest): IDbTranslationResponse =
        transactionWrapper {
            DbTranslationResponseOk(data = saveObj(req.translation))
        }

    private fun read(id: KnthTranslationId): IDbTranslationResponse {
        val res = translationTable.selectAll().where {
            translationTable.id eq id.asString()
        }.singleOrNull() ?: return errorNotFound(id)
        return DbTranslationResponseOk(translationTable.from(res))
    }

    override suspend fun readTranslation(req: DbTranslationIdRequest): IDbTranslationResponse =
        transactionWrapper { read(id = req.id) }

    private suspend fun update(
        id: KnthTranslationId,
        lock: KnthTranslationLock,
        block: (KnthTranslation) -> IDbTranslationResponse
    ): IDbTranslationResponse =
        transactionWrapper {
            if (id == KnthTranslationId.NONE) return@transactionWrapper errorEmptyId

            val current = translationTable.selectAll().where { translationTable.id eq id.asString() }
                .singleOrNull()
                ?.let { translationTable.from(it) }

            when {
                current == null -> errorNotFound(id)
                current.lock != lock -> errorRepoConcurrency(current, lock)
                else -> block(current)
            }
        }


    override suspend fun updateTranslation(req: DbTranslationRequest): IDbTranslationResponse =
        update(
            id = req.translation.id,
            lock = req.translation.lock
        ) {
            translationTable.update(
                where = {
                    translationTable.id eq req.translation.id.asString()
                }
            ) {
                to(it, req.translation.copy(lock = KnthTranslationLock(randomUuid())), randomUuid)
            }
            read(req.translation.id)
        }

    override suspend fun deleteTranslation(req: DbTranslationIdRequest): IDbTranslationResponse = update(
        id = req.id,
        lock = req.lock,
    ) {
        translationTable.deleteWhere {
            id eq req.id.asString()
        }
        DbTranslationResponseOk(it)
    }

    override suspend fun searchTranslation(req: DbTranslationFilterRequest): IDbTranslationsResponse =
        transactionWrapper(
            mainBlock = {
                val resultQuery: Query = translationTable.selectAll().where {
                    buildList {
                        add(Op.TRUE)
                        if (req.ownerId != KnthUserId.NONE) {
                            add(translationTable.ownerId eq req.ownerId.asString())
                        }
                        if (req.contentFilter.isNotBlank()) {
                            add((translationTable.content like "%${req.contentFilter}%"))
                        }
                    }.reduce { a, b -> a and b }
                }
                DbTranslationsResponseOk(data = resultQuery.map { row: ResultRow ->
                    translationTable.from(row)
                })
            },
            exceptionHandler = {
                DbTranslationsResponseErr(it.asKnthError())
            }
        )

    fun clear(): Unit = transaction(conn) {
        translationTable.deleteAll()
    }
}
