package io.dpopkov.knowthenixkbd.mappers.v1

import io.dpopkov.knowthenixkbd.api.v1.models.*
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.exceptions.UnknownKnthCommand
import io.dpopkov.knowthenixkbd.common.models.*

fun KnthContext.toTransportTranslation(): IResponse = when (val cmd = command) {
    KnthCommand.CREATE -> toTransportCreate()
    KnthCommand.READ -> toTransportRead()
    KnthCommand.UPDATE -> toTransportUpdate()
    KnthCommand.DELETE -> toTransportDelete()
    KnthCommand.SEARCH -> toTransportSearch()
    KnthCommand.NONE -> throw UnknownKnthCommand(cmd)
}

fun KnthContext.toTransportCreate() = TranslationCreateResponse(
    result = this.state.toResult(),
    errors = this.errors.toTransportErrors(),
    translation = this.translationResponse.toTransport()
)

fun KnthContext.toTransportRead() = TranslationReadResponse(
    result = this.state.toResult(),
    errors = this.errors.toTransportErrors(),
    translation = this.translationResponse.toTransport()
)

fun KnthContext.toTransportUpdate() = TranslationUpdateResponse(
    result = this.state.toResult(),
    errors = this.errors.toTransportErrors(),
    translation = this.translationResponse.toTransport()
)

fun KnthContext.toTransportDelete() = TranslationDeleteResponse(
    result = this.state.toResult(),
    errors = this.errors.toTransportErrors(),
    translation = this.translationResponse.toTransport()
)

fun KnthContext.toTransportSearch() = TranslationSearchResponse(
    result = this.state.toResult(),
    errors = this.errors.toTransportErrors(),
    translations = this.translationsResponse.toTransport()
)

private fun KnthState.toResult(): ResponseResult? = when (this) {
    KnthState.RUNNING -> ResponseResult.SUCCESS
    KnthState.FAILING -> ResponseResult.ERROR
    KnthState.FINISHING -> ResponseResult.SUCCESS
    KnthState.NONE -> null
}

private fun List<KnthError>.toTransportErrors(): List<Error>? = this
    .map {
        it.toTransport()
    }
    .toList()
    .takeIf {
        it.isNotEmpty()
    }

private fun KnthError.toTransport() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun KnthTranslation.toTransport(): TranslationResponseObject = TranslationResponseObject(
    id = id.takeIf { it != KnthTranslationId.NONE }?.asString(),
    originalId = originalId.takeIf { it != KnthTranslationId.NONE }?.asString(),
    language = language.takeIf { it.isNotBlank() },
    content = content.takeIf { it.isNotBlank() },
    syntax = this.syntax.toTransport(),
    trType = this.type.toTransport(),
    state = this.state.toTransport(),
    visibility = visibility.toTransport(),
    ownerId = ownerId.takeIf { it != KnthUserId.NONE }?.asString(),
    permissions = permissionsClient.toTransport(),
)

private fun List<KnthTranslation>.toTransport(): List<TranslationResponseObject>? = this
    .map { it.toTransport() }
    .takeIf { it.isNotEmpty()}

private fun KnthSyntaxType.toTransport(): SyntaxType? = when(this) {
    KnthSyntaxType.PLAIN_TEXT -> SyntaxType.PLAIN_TEXT
    KnthSyntaxType.MARKDOWN -> SyntaxType.MARKDOWN
    KnthSyntaxType.HTML -> SyntaxType.HTML
    KnthSyntaxType.NONE -> null
}

private fun KnthTranslationType.toTransport(): TranslationType? = when(this) {
    KnthTranslationType.QUESTION -> TranslationType.QUESTION
    KnthTranslationType.ANSWER -> TranslationType.ANSWER
    KnthTranslationType.ARTICLE -> TranslationType.ARTICLE
    KnthTranslationType.TUTORIAL -> TranslationType.TUTORIAL
    KnthTranslationType.NONE -> null
}

private fun KnthTranslationState.toTransport(): TranslationState? = when(this) {
    KnthTranslationState.NEW -> TranslationState.NEW
    KnthTranslationState.EDITED -> TranslationState.EDITED
    KnthTranslationState.TO_VERIFY -> TranslationState.TO_VERIFY
    KnthTranslationState.VERIFIED -> TranslationState.VERIFIED
    KnthTranslationState.NONE -> null
}

private fun KnthVisibility.toTransport(): TranslationVisibility? = when(this) {
    KnthVisibility.VISIBLE_PUBLIC -> TranslationVisibility.PUBLIC
    KnthVisibility.VISIBLE_TO_REGISTERED_ONLY -> TranslationVisibility.REGISTERED_ONLY
    KnthVisibility.VISIBLE_TO_OWNER -> TranslationVisibility.AUTHOR_ONLY
    KnthVisibility.NONE -> null
}

private fun Set<KnthTranslationPermissionClient>.toTransport(): Set<TranslationPermissions>? = this
    .map { it.toTransport() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun KnthTranslationPermissionClient.toTransport(): TranslationPermissions = when(this) {
    KnthTranslationPermissionClient.READ -> TranslationPermissions.READ
    KnthTranslationPermissionClient.UPDATE -> TranslationPermissions.UPDATE
    KnthTranslationPermissionClient.DELETE -> TranslationPermissions.DELETE
    KnthTranslationPermissionClient.MAKE_VISIBLE_PUBLIC -> TranslationPermissions.MAKE_VISIBLE_PUBLIC
    KnthTranslationPermissionClient.MAKE_VISIBLE_REGISTERED -> TranslationPermissions.MAKE_VISIBLE_REGISTERED
    KnthTranslationPermissionClient.MAKE_VISIBLE_OWNER -> TranslationPermissions.MAKE_VISIBLE_OWN
}
