package io.dpopkov.knowthenixkbd.common.models

data class KnthTranslation(
    var id: KnthTranslationId = KnthTranslationId.NONE,
    var originalId: KnthTranslationId = KnthTranslationId.NONE,
    var language: String = "",
    var content: String = "",
    var syntax: KnthSyntaxType = KnthSyntaxType.NONE,
    var type: KnthTranslationType = KnthTranslationType.NONE,
    var state: KnthTranslationState = KnthTranslationState.NONE,
    var aggregateId: KnthAggregateId = KnthAggregateId.NONE,
    var ownerId: KnthUserId = KnthUserId.NONE,
    var visibility: KnthVisibility = KnthVisibility.NONE,
    var lock: KnthTranslationLock = KnthTranslationLock.NONE,
    val permissionsClient: MutableSet<KnthTranslationPermissionClient> = mutableSetOf(),
) {
    fun deepCopy(): KnthTranslation = copy(
        permissionsClient = permissionsClient.toMutableSet()  // создает новый set
    )
    fun isEmpty(): Boolean = this == NONE

    fun isNotEmpty(): Boolean = this != NONE

    companion object {
        private val NONE = KnthTranslation()
    }
}
