package io.dpopkov.knowthenixkbd.repo.common

import io.dpopkov.knowthenixkbd.common.models.KnthTranslation

/**
 * Делегат для всех репозиториев, позволяющий инициализировать базу данных предзагруженными данными.
 */
class TranslationRepoInitialized(
    private val repo: IRepoTranslationInitializable,
    initObjects: Collection<KnthTranslation> = emptyList(),
) : IRepoTranslationInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<KnthTranslation> = save(initObjects).toList()
}
