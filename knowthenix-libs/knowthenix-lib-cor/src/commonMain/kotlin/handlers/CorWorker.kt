package io.dpopkov.knowthenixkbd.cor.handlers

/**
 * Одинарный обработчик шага бизнес-логики. Реализует одну операцию.
 * Использует [blockOn] для проверки условия выполнения обработки (по умолчанию true).
 * Функция [blockHandle] должна содержать код обработки (по умолчанию пустая).
 * Функция [blockExcept] - обработка исключения (по умолчанию пустая).
 */
class CorWorker<T>(
    title: String,
    description: String = "",
    blockOn: suspend T.() -> Boolean = { true },
    private val blockHandle: suspend T.() -> Unit = {},
    blockExcept: suspend T.(Throwable) -> Unit = {},
) : AbstractCorExec<T>(title, description, blockOn, blockExcept) {
    override suspend fun handle(context: T) {
        blockHandle(context)
    }
}
