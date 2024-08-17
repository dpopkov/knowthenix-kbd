package io.dpopkov.knowthenixkbd.repo.postgresql

import io.dpopkov.knowthenixkbd.common.repo.IRepoTranslation
import io.dpopkov.knowthenixkbd.repo.common.IRepoTranslationInitializable
import io.dpopkov.knowthenixkbd.repo.common.TranslationRepoInitialized
import io.dpopkov.knowthenixkbd.repo.tests.*
import kotlin.test.AfterTest

class RepoTranslationSqlCreateTest : RepoTranslationCreateTest() {
    override val repo: IRepoTranslationInitializable = SqlTestCompanion.repoUnderTestContainer(
        initObjects = initObjects,
        randomUuid = { uuidNew.asString() }
    )

    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoTranslationSqlReadTest : RepoTranslationReadTest() {
    override val repo: IRepoTranslationInitializable = SqlTestCompanion.repoUnderTestContainer(initObjects)

    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoTranslationSqlUpdateTest : RepoTranslationUpdateTest() {
    override val repo: IRepoTranslationInitializable = SqlTestCompanion.repoUnderTestContainer(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )

    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoTranslationSqlDeleteTest : RepoTranslationDeleteTest() {
    override val repo: IRepoTranslationInitializable = SqlTestCompanion.repoUnderTestContainer(initObjects)

    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoTranslationSqlSearchTest : RepoTranslationSearchTest() {
    override val repo: IRepoTranslationInitializable = SqlTestCompanion.repoUnderTestContainer(initObjects)

    @AfterTest
    fun tearDown() = repo.clear()
}

private fun IRepoTranslation.clear() {
    val pgRepo = (this as TranslationRepoInitialized).repo as RepoTranslationSql
    pgRepo.clear()
}
