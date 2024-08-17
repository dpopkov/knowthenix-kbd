package io.dpopkov.knowthenixkbd.cor

/**
 * Блок кода, который обрабатывает контекст.
 * [T] - тип контекста.
 */
interface ICorExec<T> {
    /** Наименование блока */
    val title: String
    /** Описание блока */
    val description: String

    /**
     * Обработка [context]-а.
     */
    suspend fun exec(context: T)
}
