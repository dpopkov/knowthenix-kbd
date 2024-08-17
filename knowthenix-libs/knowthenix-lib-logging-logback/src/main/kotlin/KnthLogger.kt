package io.dpopkov.knowthenixkbd.logging.jvm

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import io.dpopkov.knowthenixkbd.logging.common.IKnthLogWrapper
import kotlin.reflect.KClass

/**
 * Generate internal KnthLogWrapper using [logbackLogger].
 *
 * @param logbackLogger Logback instance from [LoggerFactory.getLogger()]
 */
fun knthLoggerLogback(logbackLogger: Logger): IKnthLogWrapper = KnthLogWrapperLogback(
    logbackLogger = logbackLogger,
    loggerId = logbackLogger.name,
)

/**
 * Generate internal KnthLogWrapper using Logback and [clazz] instance.
 */
fun knthLoggerLogback(clazz: KClass<*>): IKnthLogWrapper =
    knthLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)

/**
 * Generate internal KnthLogWrapper using Logback and [loggerId] identifier.
 */
fun knthLoggerLogback(loggerId: String): IKnthLogWrapper =
    knthLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)
