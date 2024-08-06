package io.dpopkov.knowthenixkbd.app.ktorjvm.repo

import io.dpopkov.knowthenixkbd.api.v1.models.TranslationRequestDebugMode
import io.dpopkov.knowthenixkbd.app.ktorjvm.KnthAppSettings
import io.dpopkov.knowthenixkbd.common.KnthCorSettings
import io.dpopkov.knowthenixkbd.common.repo.IRepoTranslation
import io.dpopkov.knowthenixkbd.repo.common.TranslationRepoInitialized
import io.dpopkov.knowthenixkbd.repo.inmemory.TranslationRepoInMemory

class V1TranslationRepoInMemoryTest : V1TranslationRepoBaseTest() {
    override val workMode = TranslationRequestDebugMode.TEST

    override val appSettingsCreate: KnthAppSettings = appSettingsWithRepo(
        repo = TranslationRepoInitialized(TranslationRepoInMemory(randomUuid = { uuidNew }))
    )
    override val appSettingsRead: KnthAppSettings = appSettingsWithRepo(
        repo = TranslationRepoInitialized(
            TranslationRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(super.initTranslation),
        )
    )
    override val appSettingsUpdate: KnthAppSettings = appSettingsWithRepo(
        repo = TranslationRepoInitialized(
            TranslationRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(super.initTranslation),
        )
    )
    override val appSettingsDelete: KnthAppSettings = appSettingsWithRepo(
        repo = TranslationRepoInitialized(
            TranslationRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(super.initTranslation),
        )
    )
    override val appSettingsSearch: KnthAppSettings = appSettingsWithRepo(
        repo = TranslationRepoInitialized(
            TranslationRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(super.initTranslation),
        )
    )

    private fun appSettingsWithRepo(repo: IRepoTranslation) = KnthAppSettings(
        corSettings = KnthCorSettings(
            repoTest = repo
        )
    )

}
