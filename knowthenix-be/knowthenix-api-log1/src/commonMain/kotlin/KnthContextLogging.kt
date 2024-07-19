package io.dpopkov.knowthenixkbd.api.log1.mapper

import io.dpopkov.knowthenixkbd.api.log1.models.*
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.models.*
import kotlinx.datetime.Clock

fun KnthContext.toLogModel(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "knowthenix",
    translation = toKnthLogModel(),
    errors = this.errors.map { it.toLogModel() },
)

private fun KnthContext.toKnthLogModel(): KnthTranslationLogModel? = KnthTranslationLogModel(
    requestId = this.requestId.takeIf { it != KnthRequestId.NONE }?.asString(),
    requestTranslation = this.translationRequest.takeIf { it.isNotEmpty() }?.toLogModel(),
    responseTranslation = this.translationResponse.takeIf { it.isNotEmpty() }?.toLogModel(),
    responseTranslations = this.translationsResponse.takeIf { it.isNotEmpty() }
        ?.filter { it.isNotEmpty() }
        ?.map { it.toLogModel() },
    requestFilter = this.translationFilterRequest.takeIf { it != KnthTranslationFilter() }?.toLogModel()
).takeIf { it != KnthTranslationLogModel() }

private fun KnthTranslation.toLogModel() = TranslationLog(
    id = this.id.takeIf { it != KnthTranslationId.NONE }?.asString(),
    originalId = this.originalId.takeIf { it != KnthTranslationId.NONE }?.asString(),
    language = this.language.takeIf { it.isNotBlank() },
    content = this.content.takeIf { it.isNotBlank() },
    syntax = this.syntax.takeIf { it != KnthSyntaxType.NONE }?.name,
    trType = this.type.takeIf { it != KnthTranslationType.NONE }?.name,
    state = this.state.takeIf { it != KnthTranslationState.NONE }?.name,
    visibility = this.visibility.takeIf { it != KnthVisibility.NONE }?.name,
    ownerId = this.ownerId.takeIf { it != KnthUserId.NONE }?.asString(),
    permissions = this.permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet()
)

private fun KnthTranslationFilter.toLogModel() = TranslationFilterLog(
    searchString = this.searchString.takeIf { it.isNotBlank() },
    ownerId = this.ownerId.takeIf { it != KnthUserId.NONE }?.asString(),
)

private fun KnthError.toLogModel() = ErrorLogModel(
    message = this.message.takeIf { it.isNotBlank() },
    field = this.field.takeIf { it.isNotBlank() },
    code = this.code.takeIf { it.isNotBlank() },
    level = this.level.name,
)
