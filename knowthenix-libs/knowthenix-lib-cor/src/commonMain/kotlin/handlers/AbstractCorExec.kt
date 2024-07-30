package io.dpopkov.knowthenixkbd.cor.handlers

import io.dpopkov.knowthenixkbd.cor.ICorExec

/**
 * Базовый класс для блока кода, обрабатывающего контекст.
 * Выполняется по заданному условию и может обрабатывать исключения.
 * Получает в конструктор функции:
 * - для проверки условия выполнения - [blockOn]
 * - и обработки исключений - [blockExcept].
 *
 * Содержит default реализацию [exec], использующую вызовы сервисных функций.
 * Реализующий подкласс должен имплементировать [handle] - специализированный сервисный метод обработки контекста.
 */
abstract class AbstractCorExec<T>(
    override val title: String,
    override val description: String = "",
    /** Блок проверяющий должен ли обработчик выполняться */
    private val blockOn: suspend T.() -> Boolean = { true },
    /** Блок для обработки исключений */
    private val blockExcept: suspend T.(Throwable) -> Unit = { throw it },
): ICorExec<T> {
    protected abstract suspend fun handle(context: T)

    private suspend fun on(context: T): Boolean = context.blockOn()

    private suspend fun except(context: T, e: Throwable) = context.blockExcept(e)

    /**
     * Общая обработка [context]-а с проверкой условия выполнения и обработкой исключений.
     * Сервисные функции для проверки условия и обработки исключений задаются в конструкторе.
     */
    override suspend fun exec(context: T) {
        if (on(context)) {
            try {
                handle(context)
            } catch (e: Throwable) {
                except(context, e)
            }
        }
    }
}
