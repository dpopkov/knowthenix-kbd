package io.dpopkov.knowthenixkbd.mappers.v1

import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.*
import io.dpopkov.knowthenixkbd.common.stubs.KnthTranslationStubs
import io.dpopkov.knowthenixkbd.mappers.v1.exceptions.UnknownRequestClass

fun KnthContext.fromTransport(request: IRequest): Unit = when(request) {
    is TranslationCreateRequest -> fromCreate(request)
    is TranslationReadRequest -> fromRead(request)
    is TranslationUpdateRequest -> fromUpdate(request)
    is TranslationDeleteRequest -> fromDelete(request)
    is TranslationSearchRequest -> fromSearch(request)
    else -> {
        throw UnknownRequestClass(request.javaClass)
    }
}

/*
 * Types of requests mapping
 */
private fun KnthContext.fromCreate(request: TranslationCreateRequest) {
    command = KnthCommand.CREATE
    requestId = request.requestId()
    translationRequest = request.translation?.toInternal() ?: KnthTranslation()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun KnthContext.fromRead(request: TranslationReadRequest) {
    command = KnthCommand.READ
    requestId = request.requestId()
    translationRequest = request.translation?.id.toTranslationWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun KnthContext.fromUpdate(request: TranslationUpdateRequest) {
    command = KnthCommand.UPDATE
    requestId = request.requestId()
    translationRequest = request.translation?.toInternal() ?: KnthTranslation()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun KnthContext.fromDelete(request: TranslationDeleteRequest) {
    command = KnthCommand.DELETE
    requestId = request.requestId()
    translationRequest = request.translation?.id.toTranslationWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun KnthContext.fromSearch(request: TranslationSearchRequest) {
    command = KnthCommand.SEARCH
    requestId = request.requestId()
    translationFilterRequest = request.translationFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

/*
 * ID mapping
 */
private fun String?.toTranslationId(): KnthTranslationId = this?.let { KnthTranslationId(it) } ?: KnthTranslationId.NONE
private fun String?.toTranslationWithId() = KnthTranslation(id = this.toTranslationId())
private fun IRequest?.requestId(): KnthRequestId = this?.requestId?.let { KnthRequestId(it) } ?: KnthRequestId.NONE

/*
 * Mapping helpers
 */
private fun TranslationCreateObject.toInternal(): KnthTranslation = KnthTranslation(
    language = this.language ?: "",
    formatSyntax = this.formatSyntax.fromTransport(),
    content = this.content ?: "",
    state = this.state.fromTransport(),
    visibility = this.visibility.fromTransport(),
)

private fun TranslationUpdateObject.toInternal(): KnthTranslation = KnthTranslation(
    id = this.id.toTranslationId(),
    language = this.language ?: "",
    formatSyntax = this.formatSyntax.fromTransport(),
    content = this.content ?: "",
    state = this.state.fromTransport(),
    visibility = this.visibility.fromTransport(),
)

private fun TranslationSearchFilter?.toInternal() = KnthTranslationFilter(
    searchString = this?.searchString ?: ""
)

/*
 * Enum Mappers.
 * Все экземпляры enum преобразуются один в один для контроля над возможными ошибками при будущем расширении.
 */
private fun TranslationDebug?.transportToWorkMode(): KnthWorkMode = when(this?.mode) {
    TranslationRequestDebugMode.PROD -> KnthWorkMode.PROD
    TranslationRequestDebugMode.TEST -> KnthWorkMode.TEST
    TranslationRequestDebugMode.STUB -> KnthWorkMode.STUB
    null -> KnthWorkMode.PROD
}

private fun TranslationDebug?.transportToStubCase(): KnthTranslationStubs = when(this?.stub) {
    TranslationRequestDebugStubs.SUCCESS -> KnthTranslationStubs.SUCCESS
    TranslationRequestDebugStubs.NOT_FOUND -> KnthTranslationStubs.NOT_FOUND
    TranslationRequestDebugStubs.BAD_ID -> KnthTranslationStubs.BAD_ID
    TranslationRequestDebugStubs.BAD_LANGUAGE -> KnthTranslationStubs.BAD_LANGUAGE
    TranslationRequestDebugStubs.BAD_FORMAT_SYNTAX -> KnthTranslationStubs.BAD_FORMAT_SYNTAX
    TranslationRequestDebugStubs.BAD_CONTENT -> KnthTranslationStubs.BAD_CONTENT
    TranslationRequestDebugStubs.BAD_STATE -> KnthTranslationStubs.BAD_STATE
    TranslationRequestDebugStubs.BAD_VISIBILITY -> KnthTranslationStubs.BAD_VISIBILITY
    TranslationRequestDebugStubs.CANNOT_DELETE -> KnthTranslationStubs.CANNOT_DELETE
    TranslationRequestDebugStubs.BAD_SEARCH_STRING -> KnthTranslationStubs.BAD_SEARCH_STRING
    null -> KnthTranslationStubs.NONE
}

private fun TranslationSyntax?.fromTransport(): KnthFormatSyntax = when(this) {
    TranslationSyntax.PLAIN_TEXT -> KnthFormatSyntax.PLAIN_TEXT
    TranslationSyntax.MARKDOWN -> KnthFormatSyntax.MARKDOWN
    TranslationSyntax.HTML -> KnthFormatSyntax.HTML
    null -> KnthFormatSyntax.NONE
}

private fun TranslationState?.fromTransport(): KnthTranslationState = when(this) {
    TranslationState.EDITABLE -> KnthTranslationState.EDITABLE
    TranslationState.NON_EDITABLE -> KnthTranslationState.NON_EDITABLE
    null -> KnthTranslationState.NONE
}

private fun TranslationVisibility?.fromTransport(): KnthVisibility = when(this) {
    TranslationVisibility.AUTHOR_ONLY -> KnthVisibility.VISIBLE_TO_AUTHOR
    TranslationVisibility.GROUP_ONLY -> KnthVisibility.VISIBLE_TO_GROUP
    TranslationVisibility.REGISTERED_ONLY -> KnthVisibility.VISIBLE_TO_REGISTERED
    TranslationVisibility.PUBLIC -> KnthVisibility.VISIBLE_PUBLIC
    null -> KnthVisibility.NONE
}
