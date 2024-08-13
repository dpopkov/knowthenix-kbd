package io.dpopkov.knowthenixkbd.repo.postgresql

import io.dpopkov.knowthenixkbd.common.models.*
import io.dpopkov.knowthenixkbd.repo.postgresql.enums.trStateEnumeration
import io.dpopkov.knowthenixkbd.repo.postgresql.enums.trSyntaxEnumeration
import io.dpopkov.knowthenixkbd.repo.postgresql.enums.trTypeEnumeration
import io.dpopkov.knowthenixkbd.repo.postgresql.enums.visibilityEnumeration
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder

class TranslationTable(tableName: String) : Table(tableName) {
    val id = text(SqlFields.ID)
    val originalId = text(SqlFields.ORIGINAL_ID).nullable()
    val language = text(SqlFields.LANGUAGE)
    val content = text(SqlFields.CONTENT)
    val syntax = trSyntaxEnumeration(SqlFields.SYNTAX)
    val type = trTypeEnumeration(SqlFields.TR_TYPE)
    val state = trStateEnumeration(SqlFields.STATE)
    val aggregateId = text(SqlFields.AGGREGATE_ID).nullable()
    val ownerId = text(SqlFields.OWNER_ID)
    val visibility = visibilityEnumeration(SqlFields.VISIBILITY)
    val lock = text(SqlFields.LOCK)

    override val primaryKey = PrimaryKey(id)

    fun from(res: ResultRow) = KnthTranslation(
        id = KnthTranslationId(res[id]),
        originalId = res[originalId].takeIf { it != null }?.let { KnthTranslationId(it) } ?: KnthTranslationId.NONE,
        language = res[language],
        content = res[content],
        syntax = res[syntax],
        type = res[type],
        state = res[state],
        aggregateId = res[aggregateId].takeIf { it != null }?.let { KnthAggregateId(it) } ?: KnthAggregateId.NONE,
        ownerId = KnthUserId(res[ownerId]),
        visibility = res[visibility],
        lock = KnthTranslationLock(res[lock])
    )

    fun to(builder: UpdateBuilder<*>, tr: KnthTranslation, randomUuid: () -> String) {
        builder[id] = tr.id.takeIf { it != KnthTranslationId.NONE }?.asString() ?: randomUuid()
        builder[originalId] = tr.originalId.takeIf { it != KnthTranslationId.NONE }?.asString() ?: ""
        builder[language] = tr.language
        builder[content] = tr.content
        builder[syntax] = tr.syntax
        builder[type] = tr.type
        builder[state] = tr.state
        builder[aggregateId] = tr.aggregateId.takeIf { it != KnthAggregateId.NONE }?.asString() ?: ""
        builder[ownerId] = tr.ownerId.takeIf { it != KnthUserId.NONE }?.asString() ?: ""
        builder[visibility] = tr.visibility
        builder[lock] = tr.lock.takeIf { it != KnthTranslationLock.NONE }?.asString() ?: randomUuid()
    }
}
