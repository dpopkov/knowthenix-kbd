package io.dpopkov.knowthenixkbd.repo.postgresql.enums

import io.dpopkov.knowthenixkbd.common.models.KnthVisibility
import io.dpopkov.knowthenixkbd.repo.postgresql.SqlFields
import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject

fun Table.visibilityEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.VISIBILITY_TYPE,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.VISIBILITY_OWNER -> KnthVisibility.VISIBLE_TO_OWNER
            SqlFields.VISIBILITY_REGISTERED -> KnthVisibility.VISIBLE_TO_REGISTERED_ONLY
            SqlFields.VISIBILITY_PUBLIC -> KnthVisibility.VISIBLE_PUBLIC
            else -> KnthVisibility.NONE
        }

    },
    toDb = { value ->
        when (value) {
            KnthVisibility.VISIBLE_TO_OWNER -> PgVisibilityOwner
            KnthVisibility.VISIBLE_TO_REGISTERED_ONLY -> PgVisibilityRegistered
            KnthVisibility.VISIBLE_PUBLIC -> PgVisibilityPublic
            KnthVisibility.NONE -> throw Exception("Wrong value of Visibility. NONE is unsupported")
        }
    },
)

sealed class PgVisibilityValue(eValue: String) : PGobject() {
    init {
        type = SqlFields.VISIBILITY_TYPE
        value = eValue
    }
}

object PgVisibilityPublic : PgVisibilityValue(SqlFields.VISIBILITY_PUBLIC) {
    private fun readResolve(): Any = PgVisibilityPublic
}

object PgVisibilityOwner : PgVisibilityValue(SqlFields.VISIBILITY_OWNER) {
    private fun readResolve(): Any = PgVisibilityOwner
}

object PgVisibilityRegistered : PgVisibilityValue(SqlFields.VISIBILITY_REGISTERED) {
    private fun readResolve(): Any = PgVisibilityRegistered
}
