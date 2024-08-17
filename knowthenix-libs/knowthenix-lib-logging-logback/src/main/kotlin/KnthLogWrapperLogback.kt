package io.dpopkov.knowthenixkbd.logging.jvm

import ch.qos.logback.classic.Logger
import net.logstash.logback.argument.StructuredArguments
import org.slf4j.Marker
import org.slf4j.event.KeyValuePair
import org.slf4j.event.Level
import org.slf4j.event.LoggingEvent
import io.dpopkov.knowthenixkbd.logging.common.IKnthLogWrapper
import io.dpopkov.knowthenixkbd.logging.common.LogLevel
import java.time.Instant

class KnthLogWrapperLogback(
    /**
     * Экземпляр логгера Logback.
     */
    val logbackLogger: Logger,
    /**
     * Идентификатор логгера.
     * Пробрасывается в Logback и замещает loggerClass.
     * Также используется в сообщения логгера о входе и выходе из функции.
     */
    override val loggerId: String
) : IKnthLogWrapper {
    override fun log(
        msg: String,
        level: LogLevel,
        marker: String,
        e: Throwable?,
        /** Данные численных метрик */
        data: Any?,
        /** Объекты-параметры подставляемые в лог Logback */
        objs: Map<String, Any>?
    ) = logToLogback(
        msg = msg,
        level = level.toSlf(),
        marker = DefaultMarker(marker),
        e = e,
        data = data,
        objs = objs,
    )

    /**
     * Основная функция для логирования (в Logback).
     */
    private fun logToLogback(
        msg: String = "",
        level: Level = Level.TRACE,
        marker: Marker = DefaultMarker("DEV"),
        e: Throwable? = null,
        data: Any? = null,
        objs: Map<String, Any>? = null,
    ) {
        logbackLogger.log(object : LoggingEvent {
            override fun getThrowable() = e
            override fun getTimeStamp(): Long = Instant.now().toEpochMilli()
            override fun getThreadName(): String = Thread.currentThread().name
            override fun getMessage(): String = msg
            override fun getArguments(): MutableList<Any> = argumentArray.toMutableList()

            // argument array строится из параметров data и objs
            override fun getArgumentArray(): Array<out Any> = data
                ?.let { d ->
                    listOfNotNull(
                        objs?.map {
                            StructuredArguments.keyValue(it.key, it.value)
                        }?.toTypedArray(),
                        StructuredArguments.keyValue("data", d),
                    ).toTypedArray()
                }
                ?: objs?.mapNotNull { StructuredArguments.keyValue(it.key, it.value) }?.toTypedArray()
                ?: emptyArray()

            override fun getMarkers(): MutableList<Marker> = mutableListOf(marker)
            override fun getKeyValuePairs(): MutableList<KeyValuePair> = objs
                ?.mapNotNull {
                    it.let { KeyValuePair(it.key, it.value) }
                }
                ?.toMutableList()
                ?: mutableListOf()

            override fun getLevel(): Level = level
            override fun getLoggerName(): String = logbackLogger.name
        })
    }

    private fun LogLevel.toSlf() = when (this) {
        LogLevel.ERROR -> Level.ERROR
        LogLevel.WARN -> Level.WARN
        LogLevel.INFO -> Level.INFO
        LogLevel.DEBUG -> Level.DEBUG
        LogLevel.TRACE -> Level.TRACE
    }
}
