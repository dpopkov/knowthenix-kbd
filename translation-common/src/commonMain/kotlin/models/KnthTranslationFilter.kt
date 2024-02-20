package io.dpopkov.knowthenixkbd.common.models

data class KnthTranslationFilter(
    var searchString: String = "",
    var ownerId: KnthUserId = KnthUserId.NONE,
    var questionId: KnthQuestionId = KnthQuestionId.NONE,
)
