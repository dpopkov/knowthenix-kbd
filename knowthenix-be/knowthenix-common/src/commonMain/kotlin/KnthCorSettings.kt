package io.dpopkov.knowthenixkbd.common

import io.dpopkov.knowthenixkbd.common.repo.IRepoTranslation
import io.dpopkov.knowthenixkbd.common.ws.IKnthWsSessionRepo
import io.dpopkov.knowthenixkbd.logging.common.KnthLoggerProvider

data class KnthCorSettings(
    val loggerProvider: KnthLoggerProvider = KnthLoggerProvider(),
    /** Реестр всех установленных ws сессий */
    val wsSessions: IKnthWsSessionRepo = IKnthWsSessionRepo.NONE,
    /** Репозиторий для работы в режиме стабов */
    val repoStub: IRepoTranslation = IRepoTranslation.NONE,
    /** Репозиторий для работы в режиме тестирования */
    val repoTest: IRepoTranslation = IRepoTranslation.NONE,
    /** Репозиторий для работы в нормальном режиме (production) */
    val repoProd: IRepoTranslation = IRepoTranslation.NONE,
) {
    companion object {
        val NONE = KnthCorSettings()
    }
}
