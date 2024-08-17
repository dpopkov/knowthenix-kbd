package io.dpopkov.knowthenixkbd.cor.dsl

import io.dpopkov.knowthenixkbd.cor.ICorExec
import io.dpopkov.knowthenixkbd.cor.handlers.CorChain
import io.dpopkov.knowthenixkbd.cor.handlers.CorWorker

@CorDslMarker
abstract class BaseCorExecBuilder<T> {
    var title: String = ""
    var description: String = ""

    protected var blockOn: suspend T.() -> Boolean = { true }
    protected var blockExcept: suspend T.(e: Throwable) -> Unit = { throw it }

    fun on(function: suspend T.() -> Boolean) {
        blockOn = function
    }

    fun except(function: suspend T.(e: Throwable) -> Unit) {
        blockExcept = function
    }

    abstract fun build(): ICorExec<T>
}

@CorDslMarker
class CorWorkerBuilder<T> : BaseCorExecBuilder<T>() {
    private var blockHandle: suspend T.() -> Unit = {}

    fun handle(function: suspend T.() -> Unit) {
        blockHandle = function
    }

    override fun build(): ICorExec<T> = CorWorker(
        title = super.title,
        description = super.description,
        blockOn = super.blockOn,
        blockHandle = this.blockHandle,
        blockExcept = super.blockExcept,
    )
}

@CorDslMarker
class CorChainBuilder<T> : BaseCorExecBuilder<T>() {
    private val corExecs = mutableListOf<BaseCorExecBuilder<T>>()

    fun worker(buildingFunction: CorWorkerBuilder<T>.() -> Unit) {
        add(CorWorkerBuilder<T>().apply(buildingFunction))
    }

    fun chain(buildingFunction: CorChainBuilder<T>.() -> Unit) {
        add(CorChainBuilder<T>().apply(buildingFunction))
    }

    override fun build(): ICorExec<T> = CorChain(
        title = super.title,
        description = super.description,
        corExecs = corExecs.map { it.build() },
        blockOn = super.blockOn,
        blockExcept = super.blockExcept,
    )

    private fun add(corExec: BaseCorExecBuilder<T>) = corExecs.add(corExec)
}

/**
 * Точка входа в dsl построения цепочек.
 */
fun <T> rootChain(function: CorChainBuilder<T>.() -> Unit) = CorChainBuilder<T>()
    .apply(function)
