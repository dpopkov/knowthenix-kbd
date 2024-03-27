package io.dpopkov.knowthenixkbd.mappers.v2

import io.dpopkov.knowthenixkbd.api.v2.models.*
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.*
import io.dpopkov.knowthenixkbd.mappers.v2.exceptions.UnknownKnthCommand

fun KnthContext.toTransportTranslationV2(): IResponse = when(val cmd = command) {
    KnthCommand.CREATE -> toTransportCreate()
    KnthCommand.READ -> toTransportRead()
    KnthCommand.UPDATE -> toTransportUpdate()
    KnthCommand.DELETE -> toTransportDelete()
    KnthCommand.SEARCH -> toTransportSearch()
    KnthCommand.NONE -> throw UnknownKnthCommand(cmd)
}

/*
 * Types of response mapping
 */
private fun KnthContext.toTransportCreate() = TranslationCreateResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = state.toResultV2(),
    errors = errors.toTransportErrors(),
    translation = translationResponse.toTransport(),
)

private fun KnthContext.toTransportRead() = TranslationReadResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = state.toResultV2(),
    errors = errors.toTransportErrors(),
    translation = translationResponse.toTransport(),
)

private fun KnthContext.toTransportUpdate() = TranslationUpdateResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = state.toResultV2(),
    errors = errors.toTransportErrors(),
    translation = translationResponse.toTransport(),
)

private fun KnthContext.toTransportDelete() = TranslationDeleteResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = state.toResultV2(),
    errors = errors.toTransportErrors(),
    translation = translationResponse.toTransport(),
)

private fun KnthContext.toTransportSearch() = TranslationSearchResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = state.toResultV2(),
    errors = errors.toTransportErrors(),
    translations = translationsResponse.toTransport()
)

/*
 * Helpers
 */
private fun MutableList<KnthError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportError() }
    .takeIf { it.isNotEmpty() }

private fun KnthError.toTransportError() = Error(
    code = this.code.takeIf { it.isNotBlank() },
    group = this.group.takeIf { it.isNotBlank() },
    field = this.field.takeIf { it.isNotBlank() },
    message = this.message.takeIf { it.isNotBlank() },
)

private fun KnthTranslation.toTransport(): TranslationResponseObject {
    return TranslationResponseObject(
        id = this.id.takeIf { it != KnthTranslationId.NONE }?.asString(),
        language = this.language.takeIf { it.isNotBlank() },
        formatSyntax = this.formatSyntax.toTransportV2(),
        content = this.content.takeIf { it.isNotBlank() },
        state = this.state.toTransportV2(),
        questionId = this.questionId.takeIf { it != KnthQuestionId.NONE }?.asString(),
        visibility = this.visibility.toTransportV2(),
        ownerId = this.ownerId.takeIf { it != KnthUserId.NONE }?.asString(),
        lock = this.lock.takeIf { it != KnthTranslationLock.NONE }?.asString(),
        permissions = this.permissionsClient.toTransport(),
    )
}

private fun MutableSet<KnthTranslationPermissionClient>.toTransport(): Set<TranslationPermissions>? = this
    .map { it.toTransport() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun List<KnthTranslation>.toTransport(): List<TranslationResponseObject>? = this
    .map { it.toTransport() }
    .takeIf { it.isNotEmpty() }

/*
 * Enum Mappers.
 * Все экземпляры enum преобразуются один в один для контроля над возможными ошибками при будущем расширении.
 */
fun KnthFormatSyntax.toTransportV2(): TranslationSyntax? = when (this) {
    KnthFormatSyntax.PLAIN_TEXT -> TranslationSyntax.PLAIN_TEXT
    KnthFormatSyntax.MARKDOWN -> TranslationSyntax.MARKDOWN
    KnthFormatSyntax.HTML -> TranslationSyntax.HTML
    KnthFormatSyntax.NONE -> null
}

fun KnthTranslationState.toTransportV2(): TranslationState? = when (this) {
    KnthTranslationState.EDITABLE -> TranslationState.EDITABLE
    KnthTranslationState.NON_EDITABLE -> TranslationState.NON_EDITABLE
    KnthTranslationState.NONE -> null
}

fun KnthVisibility.toTransportV2(): TranslationVisibility? = when (this) {
    KnthVisibility.VISIBLE_TO_AUTHOR -> TranslationVisibility.AUTHOR_ONLY
    KnthVisibility.VISIBLE_TO_GROUP -> TranslationVisibility.GROUP_ONLY
    KnthVisibility.VISIBLE_TO_REGISTERED -> TranslationVisibility.REGISTERED_ONLY
    KnthVisibility.VISIBLE_PUBLIC -> TranslationVisibility.PUBLIC
    KnthVisibility.NONE -> null
}

private fun KnthTranslationPermissionClient.toTransport(): TranslationPermissions = when (this) {
    KnthTranslationPermissionClient.READ -> TranslationPermissions.READ
    KnthTranslationPermissionClient.UPDATE -> TranslationPermissions.UPDATE
    KnthTranslationPermissionClient.DELETE -> TranslationPermissions.DELETE
    KnthTranslationPermissionClient.MAKE_VISIBLE_AUTHOR -> TranslationPermissions.MAKE_VISIBLE_AUTHOR
    KnthTranslationPermissionClient.MAKE_VISIBLE_GROUP -> TranslationPermissions.MAKE_VISIBLE_GROUP
    KnthTranslationPermissionClient.MAKE_VISIBLE_REGISTERED -> TranslationPermissions.MAKE_VISIBLE_REGISTERED
    KnthTranslationPermissionClient.MAKE_VISIBLE_PUBLIC -> TranslationPermissions.MAKE_VISIBLE_PUBLIC
}

private fun KnthState.toResultV2(): ResponseResult? = when (this) {
    KnthState.RUNNING, KnthState.FINISHING -> ResponseResult.SUCCESS
    KnthState.FAILING -> ResponseResult.ERROR
    KnthState.NONE -> null
}
