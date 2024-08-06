package io.dpopkov.knowthenixkbd.biz.validation

import io.dpopkov.knowthenixkbd.biz.KnthTranslationProcessor
import io.dpopkov.knowthenixkbd.common.KnthCorSettings
import io.dpopkov.knowthenixkbd.common.models.KnthCommand
import io.dpopkov.knowthenixkbd.repo.common.TranslationRepoInitialized
import io.dpopkov.knowthenixkbd.repo.inmemory.TranslationRepoInMemory
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStub

abstract class BaseBizValidationTest(val command: KnthCommand) {
    private val repo = TranslationRepoInitialized(
        repo = TranslationRepoInMemory(),
        initObjects = listOf(
            KnthTranslationStub.get()
        )
    )
    private val settings by lazy {
        KnthCorSettings(
            repoTest = repo
        )
    }
    protected val processor by lazy {
        KnthTranslationProcessor(
            corSettings = settings
        )
    }
}
