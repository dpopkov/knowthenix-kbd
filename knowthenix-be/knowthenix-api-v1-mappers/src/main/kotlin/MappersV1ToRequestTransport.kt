package io.dpopkov.knowthenixkbd.mappers.v1

import io.dpopkov.knowthenixkbd.api.v1.models.TranslationCreateObject
import io.dpopkov.knowthenixkbd.api.v1.models.TranslationDeleteObject
import io.dpopkov.knowthenixkbd.api.v1.models.TranslationReadObject
import io.dpopkov.knowthenixkbd.api.v1.models.TranslationUpdateObject
import io.dpopkov.knowthenixkbd.common.models.KnthTranslation
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationId
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationLock

fun KnthTranslation.toTransportCreate() = TranslationCreateObject(
    originalId = this.originalId.takeIf { it != KnthTranslationId.NONE }?.asString(),
    language = this.language.takeIf { it.isNotBlank() },
    content = this.content.takeIf { it.isNotBlank() },
    syntax = this.syntax.toTransport(),
    trType = this.type.toTransport(),
    state = this.state.toTransport(),
    visibility = this.visibility.toTransport(),
)

fun KnthTranslation.toTransportRead() = TranslationReadObject(
    id = this.id.takeIf { it != KnthTranslationId.NONE }?.asString(),
)

fun KnthTranslation.toTransportUpdate() = TranslationUpdateObject(
    id = this.id.takeIf { it != KnthTranslationId.NONE }?.asString(),
    originalId = this.originalId.takeIf { it != KnthTranslationId.NONE }?.asString(),
    language = this.language.takeIf { it.isNotBlank() },
    content = this.content.takeIf { it.isNotBlank() },
    syntax = this.syntax.toTransport(),
    trType = this.type.toTransport(),
    state = this.state.toTransport(),
    visibility = this.visibility.toTransport(),
    lock = this.lock.takeIf { it != KnthTranslationLock.NONE }?.asString(),
)

fun KnthTranslation.toTransportDelete() = TranslationDeleteObject(
    id = this.id.takeIf { it != KnthTranslationId.NONE }?.asString(),
    lock = this.lock.takeIf { it != KnthTranslationLock.NONE }?.asString(),
)
