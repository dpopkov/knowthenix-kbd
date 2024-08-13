package io.dpopkov.knowthenixkbd.repo.postgresql

object SqlFields {
    const val ID = "id"
    const val ORIGINAL_ID = "original_id"
    const val LANGUAGE = "language"
    const val CONTENT = "content"
    const val SYNTAX = "syntax"
    const val TR_TYPE = "tr_type"
    const val STATE = "state"
    const val AGGREGATE_ID = "aggregate_id"
    const val OWNER_ID = "owner_id"
    const val VISIBILITY = "visibility"
    const val LOCK = "lock"

    const val TR_SYNTAX_TYPE = "tr_syntax_type"
    const val TR_SYNTAX_PLAIN_TEXT = "plain_text"
    const val TR_SYNTAX_MARKDOWN = "markdown"
    const val TR_SYNTAX_HTML = "html"

    const val TR_TYPE_TYPE = "tr_type_type"
    const val TR_TYPE_QUESTION = "question"
    const val TR_TYPE_ANSWER = "answer"
    const val TR_TYPE_ARTICLE = "article"
    const val TR_TYPE_TUTORIAL = "tutorial"

    const val TR_STATE_TYPE = "tr_state_type"
    const val TR_STATE_NEW = "new"
    const val TR_STATE_EDITED = "edited"
    const val TR_STATE_TO_VERIFY = "to_verify"
    const val TR_STATE_VERIFIED = "verified"

    const val VISIBILITY_TYPE = "tr_visibility_type"
    const val VISIBILITY_OWNER = "owner"
    const val VISIBILITY_REGISTERED = "registered"
    const val VISIBILITY_PUBLIC = "public"
}
