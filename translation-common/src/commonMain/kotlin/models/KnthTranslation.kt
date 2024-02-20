package io.dpopkov.knowthenixkbd.common.models

data class KnthTranslation (
    var id: KnthTranslationId = KnthTranslationId.NONE,
    var ownerId: KnthUserId = KnthUserId.NONE,
    var language: String = "",
    var formatSyntax: KnthFormatSyntax = KnthFormatSyntax.NONE,
    var content: String = "",
    var state: KnthTranslationState = KnthTranslationState.NONE,
    var questionId: KnthQuestionId = KnthQuestionId.NONE,
    var visibility: KnthVisibility = KnthVisibility.NONE,
    var lock: KnthTranslationLock = KnthTranslationLock.NONE,
    val permissionsClient: MutableSet<KnthTranslationPermissionClient> = mutableSetOf()
) {
    fun isEmpty(): Boolean = this == NONE

    companion object {
        private val NONE = KnthTranslation()
    }
}
