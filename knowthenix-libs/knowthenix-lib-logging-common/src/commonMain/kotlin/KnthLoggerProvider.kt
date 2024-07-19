package io.dpopkov.knowthenixkbd.logging.common

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

/**
 * Инициализирует выбранный логгер. Конкретный логгер определяется параметром [provider],
 * в котором передается лямбда возвращающая экземпляр логгера.
 *
 * ```kotlin
 * // Обычный традиционный способ вызова логгера:
 * val logger = LoggerFactory.getLogger(this::class.java)
 * // В проекте будет создаваться экземпляр логгер-провайдера с использованием лямбды,
 * // возвращающей конкретный экземпляр логгера в зависимости от используемой
 * // библиотеки логирования (Logback или другой).
 * val loggerProvider = KnthLoggerProvider { clazz -> knthLoggerLogback(clazz) }
 *
 * // В дальнейшем этот логгер-провайдер может использоваться таким образом (один из вариантов):
 * val logger = loggerProvider.logger(this::class)
 * logger.info("My log")
 * ```
 */
class KnthLoggerProvider(
    private val provider: (String) -> IKnthLogWrapper = { IKnthLogWrapper.DEFAULT }
) {
    /**
     * Инициализирует и возвращает экземпляр логгера по идентификатору [loggerId].
     */
    fun logger(loggerId: String): IKnthLogWrapper = provider(loggerId)

    /**
     * Инициализирует и возвращает экземпляр логгера по классу [clazz].
     */
    fun logger(clazz: KClass<*>): IKnthLogWrapper = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")

    /**
     * Инициализирует и возвращает экземпляр логгера через функцию [function].
     */
    fun logger(function: KFunction<*>): IKnthLogWrapper = provider(function.name)
}
