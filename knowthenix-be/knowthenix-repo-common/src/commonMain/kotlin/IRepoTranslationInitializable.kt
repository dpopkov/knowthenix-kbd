package io.dpopkov.knowthenixkbd.repo.common

import io.dpopkov.knowthenixkbd.common.models.KnthTranslation
import io.dpopkov.knowthenixkbd.common.repo.IRepoTranslation

interface IRepoTranslationInitializable : IRepoTranslation {
    /** Предназначен только для инициализации репозитория, поэтому не является suspend методом. */
    fun save(translations: Collection<KnthTranslation>): Collection<KnthTranslation>
}
