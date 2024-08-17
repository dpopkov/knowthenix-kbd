package io.dpopkov.knowthenixkbd.repo.inmemory

import io.dpopkov.knowthenixkbd.common.models.*

/**
 * Сущность перевода для работы с базой данных.
 */
data class TranslationEntity(
    val id: String? = null,
    val originalId: String? = null,
    val language: String? = null,
    val content: String? = null,
    val syntax: String? = null,
    val type: String? = null,
    val state: String? = null,
    val aggregateId: String? = null,
    val ownerId: String? = null,
    val visibility: String? = null,
    val lock: String? = null,
) {
    constructor(model: KnthTranslation) : this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        originalId = model.originalId.asString().takeIf { it.isNotBlank() },
        language = model.language.takeIf { it.isNotBlank() },
        content = model.content.takeIf { it.isNotBlank() },
        syntax = model.syntax.takeIf { it != KnthSyntaxType.NONE }?.name,
        type = model.type.takeIf { it != KnthTranslationType.NONE }?.name,
        state = model.state.takeIf { it != KnthTranslationState.NONE }?.name,
        aggregateId = model.aggregateId.asString().takeIf { it.isNotBlank() },
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() },
        visibility = model.visibility.takeIf { it != KnthVisibility.NONE }?.name,
        lock = model.lock.asString().takeIf { it.isNotBlank() },
        // permissionsClient не сохраняются, так как они вычисляемые
    )

    fun toInternal() = KnthTranslation(
        id = id?.let { KnthTranslationId(it) } ?: KnthTranslationId.NONE,
        originalId = originalId?.let { KnthTranslationId(it) } ?: KnthTranslationId.NONE,
        language = language ?: "",
        content = content ?: "",
        syntax = syntax?.let { KnthSyntaxType.valueOf(it) } ?: KnthSyntaxType.NONE,
        type = type?.let { KnthTranslationType.valueOf(it) } ?: KnthTranslationType.NONE,
        state = state?.let { KnthTranslationState.valueOf(it) } ?: KnthTranslationState.NONE,
        aggregateId = aggregateId?.let { KnthAggregateId(it) } ?: KnthAggregateId.NONE,
        ownerId = ownerId?.let { KnthUserId(it) } ?: KnthUserId.NONE,
        visibility = visibility?.let { KnthVisibility.valueOf(it) } ?: KnthVisibility.NONE,
        lock = lock?.let { KnthTranslationLock(it) } ?: KnthTranslationLock.NONE,
    )
}
