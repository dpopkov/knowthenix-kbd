package io.dpopkov.knowthenixkbd.cor.handlers

import io.dpopkov.knowthenixkbd.cor.ICorExec

/**
 * Цепочка обработчиков, в т.ч. и вложенных chain.
 * Обрабатывает все обработчики переданные в списке [corExecs].
 * Используется для группировки обработчиков контекста типа [T]:
 * - можно иметь одну корневую цепочку и по условиям выполнять worker-ы для отдельных операций;
 * - можно иметь разные цепочки для разных операций и хранить их в map-е по ключу - операции.
 */
class CorChain<T>(
    title: String,
    description: String = "",
    private val corExecs: List<ICorExec<T>>,
    blockOn: suspend T.() -> Boolean = { true },
    blockExcept: suspend T.(Throwable) -> Unit = { throw it },
) : AbstractCorExec<T>(title, description, blockOn, blockExcept) {
    override suspend fun handle(context: T) {
        // Обработка производится последовательно, так как контекст mutable.
        // В случае реализации параллельной обработки контекстов в разных потоках
        // возможны race conditions, поэтому необходимо реализовать механизмы синхронизации.
        corExecs.onEach {
            it.exec(context)
        }
    }
}
