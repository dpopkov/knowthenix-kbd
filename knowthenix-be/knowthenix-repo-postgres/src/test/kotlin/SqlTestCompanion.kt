package io.dpopkov.knowthenixkbd.repo.postgresql

import com.benasher44.uuid.uuid4
import io.dpopkov.knowthenixkbd.common.models.KnthTranslation
import io.dpopkov.knowthenixkbd.repo.common.IRepoTranslationInitializable
import io.dpopkov.knowthenixkbd.repo.common.TranslationRepoInitialized

object SqlTestCompanion {
    private const val HOST = "localhost"
    private const val USER = "postgres"
    private const val PASS = "knowthenixkbd-pass"
    private val PORT = getEnv("postgresPort")?.toIntOrNull() ?: 5432

    fun repoUnderTestContainer(
        initObjects: Collection<KnthTranslation> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() }
    ) : IRepoTranslationInitializable = TranslationRepoInitialized(
        repo = RepoTranslationSql(
            properties = SqlProperties(
                host = HOST,
                port = PORT,
                user = USER,
                password = PASS,
            ),
            randomUuid = randomUuid,
        ),
        initObjects = initObjects,
    )

    private fun getEnv(name: String): String? = System.getenv(name)
}
