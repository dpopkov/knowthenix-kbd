package io.dpopkov.knowthenixkbd.repo.postgresql.enums

import io.dpopkov.knowthenixkbd.common.models.KnthSyntaxType
import io.dpopkov.knowthenixkbd.repo.postgresql.SqlFields
import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject


fun Table.trSyntaxEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.TR_SYNTAX_TYPE,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.TR_SYNTAX_PLAIN_TEXT -> KnthSyntaxType.PLAIN_TEXT
            SqlFields.TR_SYNTAX_MARKDOWN -> KnthSyntaxType.MARKDOWN
            SqlFields.TR_SYNTAX_HTML -> KnthSyntaxType.HTML
            else -> KnthSyntaxType.NONE
        }
    },
    toDb = { value ->
        when(value) {
            KnthSyntaxType.PLAIN_TEXT -> PgTrSyntaxPlainText
            KnthSyntaxType.MARKDOWN -> PgTrSyntaxMarkdown
            KnthSyntaxType.HTML -> PgTrSyntaxHtml
            KnthSyntaxType.NONE -> throw Exception("Wrong value of Translation Syntax. NONE is unsupported")
        }
    },
)

sealed class PgTrSyntaxValue(eValue: String) : PGobject() {
    init {
        type = SqlFields.TR_SYNTAX_TYPE
        value = eValue
    }
}

object PgTrSyntaxPlainText: PgTrSyntaxValue(SqlFields.TR_SYNTAX_PLAIN_TEXT) {
    private fun readResolve(): Any = PgTrSyntaxPlainText
}

object PgTrSyntaxMarkdown: PgTrSyntaxValue(SqlFields.TR_SYNTAX_MARKDOWN) {
    private fun readResolve(): Any = PgTrSyntaxMarkdown
}

object PgTrSyntaxHtml: PgTrSyntaxValue(SqlFields.TR_SYNTAX_HTML) {
    private fun readResolve(): Any = PgTrSyntaxHtml
}
