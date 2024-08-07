package io.dpopkov.knowthenixkbd.repo.inmemory

import io.dpopkov.knowthenixkbd.repo.common.TranslationRepoInitialized
import io.dpopkov.knowthenixkbd.repo.tests.*

class TranslationRepoInMemoryCreateTest : RepoTranslationCreateTest() {
    override val repo = TranslationRepoInitialized(
        repo = TranslationRepoInMemory(randomUuid = { super.uuidNew.asString() }),
        initObjects = Companion.initObjects
    )
}

class TranslationRepoInMemoryReadTest : RepoTranslationReadTest() {
    override val repo = TranslationRepoInitialized(
        repo = TranslationRepoInMemory(),
        initObjects = Companion.initObjects
    )
}

class TranslationRepoInMemoryUpdateTest : RepoTranslationUpdateTest() {
    override val repo = TranslationRepoInitialized(
        repo = TranslationRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = Companion.initObjects
    )
}

class TranslationRepoInMemoryDeleteTest : RepoTranslationDeleteTest() {
    override val repo = TranslationRepoInitialized(
        repo = TranslationRepoInMemory(),
        initObjects = Companion.initObjects
    )
}

class TranslationRepoInMemorySearchTest : RepoTranslationSearchTest() {
    override val repo = TranslationRepoInitialized(
        repo = TranslationRepoInMemory(),
        initObjects = Companion.initObjects
    )
}
