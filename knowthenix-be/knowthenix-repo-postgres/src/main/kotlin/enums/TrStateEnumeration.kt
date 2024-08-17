package io.dpopkov.knowthenixkbd.repo.postgresql.enums

import io.dpopkov.knowthenixkbd.common.models.KnthTranslationState
import io.dpopkov.knowthenixkbd.repo.postgresql.SqlFields
import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject

fun Table.trStateEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.TR_STATE_TYPE,
    fromDb = { value ->
        when(value.toString()) {
            SqlFields.TR_STATE_NEW -> KnthTranslationState.NEW
            SqlFields.TR_STATE_EDITED -> KnthTranslationState.EDITED
            SqlFields.TR_STATE_TO_VERIFY -> KnthTranslationState.TO_VERIFY
            SqlFields.TR_STATE_VERIFIED -> KnthTranslationState.VERIFIED
            else -> KnthTranslationState.NONE
        }
    },
    toDb = { value ->
        when(value) {
            KnthTranslationState.NEW -> PgTrStateNew
            KnthTranslationState.EDITED -> PgTrStateEdited
            KnthTranslationState.TO_VERIFY -> PgTrStateToVerify
            KnthTranslationState.VERIFIED -> PgTrStateVerified
            KnthTranslationState.NONE -> throw Exception("Wrong value of Translation State. NONE is unsupported")
        }
    }
)
sealed class PgTrStateValue(eValue: String) : PGobject() {
    init {
        type = SqlFields.TR_STATE_TYPE
        value = eValue
    }
}

object PgTrStateNew : PgTrStateValue(SqlFields.TR_STATE_NEW) {
    private fun readResolve(): Any = PgTrStateNew
}

object PgTrStateEdited : PgTrStateValue(SqlFields.TR_STATE_EDITED) {
    private fun readResolve(): Any = PgTrStateEdited
}

object PgTrStateToVerify : PgTrStateValue(SqlFields.TR_STATE_TO_VERIFY) {
    private fun readResolve(): Any = PgTrStateToVerify
}

object PgTrStateVerified : PgTrStateValue(SqlFields.TR_STATE_VERIFIED) {
    private fun readResolve(): Any = PgTrStateVerified
}
