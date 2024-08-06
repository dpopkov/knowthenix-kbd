package io.dpopkov.knowthenixkbd.mappers.v1

import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.*
import io.dpopkov.knowthenixkbd.common.stubs.KnthStubs
import io.dpopkov.knowthenixkbd.mappers.v1.exceptions.UnknownRequestClass

fun KnthContext.fromTransport(request: IRequest) = when (request) {
    is TranslationCreateRequest -> fromTransport(request)
    is TranslationReadRequest -> fromTransport(request)
    is TranslationUpdateRequest -> fromTransport(request)
    is TranslationDeleteRequest -> fromTransport(request)
    is TranslationSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toTranslationId() = this?.let { KnthTranslationId(it) } ?: KnthTranslationId.NONE

private fun String?.toTranslationLock() = this?.let { KnthTranslationLock(it) } ?: KnthTranslationLock.NONE

private fun TranslationDebug?.transportToWorkMode(): KnthWorkMode = when (this?.mode) {
    TranslationRequestDebugMode.PROD -> KnthWorkMode.PROD
    TranslationRequestDebugMode.TEST -> KnthWorkMode.TEST
    TranslationRequestDebugMode.STUB -> KnthWorkMode.STUB
    null -> KnthWorkMode.PROD
}

private fun TranslationDebug?.transportToStubCase(): KnthStubs = when (this?.stub) {
    TranslationRequestDebugStubs.SUCCESS -> KnthStubs.SUCCESS
    TranslationRequestDebugStubs.NOT_FOUND -> KnthStubs.NOT_FOUND
    TranslationRequestDebugStubs.BAD_ID -> KnthStubs.BAD_ID
    TranslationRequestDebugStubs.BAD_ORIGINAL_ID -> KnthStubs.BAD_ORIGINAL_ID
    TranslationRequestDebugStubs.BAD_LANGUAGE -> KnthStubs.BAD_LANGUAGE
    TranslationRequestDebugStubs.BAD_CONTENT -> KnthStubs.BAD_CONTENT
    TranslationRequestDebugStubs.BAD_SYNTAX -> KnthStubs.BAD_SYNTAX
    TranslationRequestDebugStubs.BAD_TR_TYPE -> KnthStubs.BAD_TR_TYPE
    TranslationRequestDebugStubs.BAD_STATE -> KnthStubs.BAD_STATE
    TranslationRequestDebugStubs.BAD_VISIBILITY -> KnthStubs.BAD_VISIBILITY
    TranslationRequestDebugStubs.CANNOT_DELETE -> KnthStubs.CANNOT_DELETE
    TranslationRequestDebugStubs.BAD_SEARCH_STRING -> KnthStubs.BAD_SEARCH_STRING
    null -> KnthStubs.NONE
}

fun KnthContext.fromTransport(request: TranslationCreateRequest) {
    command = KnthCommand.CREATE
    translationRequest = request.translation?.toInternal() ?: KnthTranslation()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun KnthContext.fromTransport(request: TranslationReadRequest) {
    command = KnthCommand.READ
    translationRequest = request.translation.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun KnthContext.fromTransport(request: TranslationUpdateRequest) {
    command = KnthCommand.UPDATE
    translationRequest = request.translation?.toInternal() ?: KnthTranslation()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun KnthContext.fromTransport(request: TranslationDeleteRequest) {
    command = KnthCommand.DELETE
    translationRequest = request.translation.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun KnthContext.fromTransport(request: TranslationSearchRequest) {
    command = KnthCommand.SEARCH
    translationFilterRequest = request.translationFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun TranslationCreateObject.toInternal() = KnthTranslation(
    originalId = this.originalId.toTranslationId(),
    language = this.language ?: "",
    content = this.content ?: "",
    syntax = this.syntax.fromTransport(),
    type = this.trType.fromTransport(),
    state = this.state.fromTransport(),
    visibility = this.visibility.fromTransport(),
)

private fun TranslationReadObject?.toInternal(): KnthTranslation = if (this != null) {
    KnthTranslation(id = id.toTranslationId())
} else {
    KnthTranslation()
}

private fun TranslationUpdateObject.toInternal() = KnthTranslation(
    id = this.id.toTranslationId(),
    originalId = this.originalId.toTranslationId(),
    language = this.language ?: "",
    content = this.content ?: "",
    syntax = this.syntax.fromTransport(),
    type = this.trType.fromTransport(),
    state = this.state.fromTransport(),
    visibility = this.visibility.fromTransport(),
    lock = this.lock.toTranslationLock()
)

private fun TranslationDeleteObject?.toInternal(): KnthTranslation = if (this != null) {
    KnthTranslation(
        id = this.id.toTranslationId(),
        lock = this.lock.toTranslationLock(),
    )
} else {
    KnthTranslation()
}

private fun TranslationSearchFilter?.toInternal() = KnthTranslationFilter(
    searchString = this?.searchString ?: "",
    ownerId = this?.ownerId?.let { KnthUserId(it) } ?: KnthUserId.NONE
)

private fun SyntaxType?.fromTransport(): KnthSyntaxType = when (this) {
    SyntaxType.PLAIN_TEXT -> KnthSyntaxType.PLAIN_TEXT
    SyntaxType.MARKDOWN -> KnthSyntaxType.MARKDOWN
    SyntaxType.HTML -> KnthSyntaxType.HTML
    null -> KnthSyntaxType.NONE
}

private fun TranslationType?.fromTransport(): KnthTranslationType = when (this) {
    TranslationType.QUESTION -> KnthTranslationType.QUESTION
    TranslationType.ANSWER -> KnthTranslationType.ANSWER
    TranslationType.ARTICLE -> KnthTranslationType.ARTICLE
    TranslationType.TUTORIAL -> KnthTranslationType.TUTORIAL
    null -> KnthTranslationType.NONE
}

private fun TranslationState?.fromTransport(): KnthTranslationState = when (this) {
    TranslationState.NEW -> KnthTranslationState.NEW
    TranslationState.EDITED -> KnthTranslationState.EDITED
    TranslationState.TO_VERIFY -> KnthTranslationState.TO_VERIFY
    TranslationState.VERIFIED -> KnthTranslationState.VERIFIED
    null -> KnthTranslationState.NONE
}

private fun TranslationVisibility?.fromTransport(): KnthVisibility = when (this) {
    TranslationVisibility.PUBLIC -> KnthVisibility.VISIBLE_PUBLIC
    TranslationVisibility.REGISTERED_ONLY -> KnthVisibility.VISIBLE_TO_REGISTERED_ONLY
    TranslationVisibility.AUTHOR_ONLY -> KnthVisibility.VISIBLE_TO_OWNER
    null -> KnthVisibility.NONE
}
