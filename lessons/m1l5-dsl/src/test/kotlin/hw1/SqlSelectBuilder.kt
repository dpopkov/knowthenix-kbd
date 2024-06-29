package io.dpopkov.knowthenixkbd.m1l5.hw1

class SqlSelectBuilder {
    private var _table: String = ""
    private val _columns: MutableList<String> = mutableListOf()
    private var _where: String = ""

    fun select(vararg cols: String) {
        _columns.addAll(cols)
    }

    fun from(table: String) {
        _table = table
    }

    fun where(block: WhereContext.() -> Unit) {
        val ctx = WhereContext().apply(block)
        _where = ctx.build()
    }

    fun build(): String {
        require(_table.isNotBlank())
        return buildString {
            append("select ")
            val columns = _columns.joinToString().ifEmpty { "*" }
            append(columns)
            append(" from ")
            append(_table)
            if (_where.isNotBlank()) {
                append(_where)
            }
        }
    }
}

@SqlDsl
abstract class ConditionBase {
    protected val conditions: MutableList<String> = mutableListOf()

    fun add(condition: String) {
        conditions.add(condition)
    }

    abstract fun build(): String
}

open class WhereContext : ConditionBase() {

    infix fun String.eq(value: String) {
        if ("null" == value) {
            add("$this is null")
        } else {
            add("$this = '$value'")
        }
    }

    infix fun String.eq(value: Number) {
        add("$this = $value")
    }

    infix fun String.nonEq(value: String) {
        if ("null" == value) {
            add("$this !is null")
        } else {
            add("$this != '$value'")
        }
    }

    infix fun String.nonEq(value: Nothing?) {
        add("$this !is null")
    }

    infix fun String.nonEq(value: Number) {
        add("$this != $value")
    }

    fun or(block: OrContext.() -> Unit) {
        val ctx = OrContext()
        add(ctx.apply(block).build())
    }

    override fun build(): String {
        require(conditions.size == 1)
        return " where ${conditions[0]}"
    }
}

class OrContext: WhereContext() {
    override fun build(): String {
        require(conditions.size == 2)
        return conditions.joinToString(
            separator = " or ",
            prefix = "(",
            postfix = ")"
        )
    }
}

fun query(block: SqlSelectBuilder.() -> Unit): SqlSelectBuilder {
    return SqlSelectBuilder().apply(block)
}

@DslMarker
annotation class SqlDsl()
