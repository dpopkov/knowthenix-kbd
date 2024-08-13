package io.dpopkov.knowthenixkbd.repo.postgresql.enums

import io.dpopkov.knowthenixkbd.common.models.KnthTranslationType
import io.dpopkov.knowthenixkbd.repo.postgresql.SqlFields
import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject

fun Table.trTypeEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.TR_TYPE_TYPE,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.TR_TYPE_QUESTION -> KnthTranslationType.QUESTION
            SqlFields.TR_TYPE_ANSWER -> KnthTranslationType.ANSWER
            SqlFields.TR_TYPE_ARTICLE -> KnthTranslationType.ARTICLE
            SqlFields.TR_TYPE_TUTORIAL -> KnthTranslationType.TUTORIAL
            else -> KnthTranslationType.NONE
        }
    },
    toDb = { value ->
        when (value) {
            KnthTranslationType.QUESTION -> PgTrTypeQuestion
            KnthTranslationType.ANSWER -> PgTrTypeAnswer
            KnthTranslationType.ARTICLE -> PgTrTypeArticle
            KnthTranslationType.TUTORIAL -> PgTrTypeTutorial
            KnthTranslationType.NONE -> throw Exception("Wrong value of Translation Type. NONE is unsupported")
        }
    },
)

sealed class PgTrTypeValue(enValue: String) : PGobject() {
    init {
        type = SqlFields.TR_TYPE_TYPE
        value = enValue
    }
}

object PgTrTypeQuestion : PgTrTypeValue(SqlFields.TR_TYPE_QUESTION) {
    private fun readResolve(): Any = PgTrTypeQuestion
}

object PgTrTypeAnswer : PgTrTypeValue(SqlFields.TR_TYPE_ANSWER) {
    private fun readResolve(): Any = PgTrTypeAnswer
}

object PgTrTypeArticle : PgTrTypeValue(SqlFields.TR_TYPE_ARTICLE) {
    private fun readResolve(): Any = PgTrTypeArticle
}

object PgTrTypeTutorial : PgTrTypeValue(SqlFields.TR_TYPE_TUTORIAL) {
    private fun readResolve(): Any = PgTrTypeTutorial
}
