package io.dpopkov.knowthenixkbd.logging.common

import kotlinx.datetime.Clock
import kotlin.time.measureTimedValue

/**
 * Контракт логгера мультиплатформенного проекта.
 * Создает фасад изолирующий от Slf4j или подобных библиотек.
 */
@OptIn(ExperimentalStdlibApi::class)
interface IKnthLogWrapper: AutoCloseable {
    val loggerId: String

    fun log(
        msg: String = "",
        level: LogLevel = LogLevel.TRACE,
        marker: String = "DEV",
        e: Throwable? = null,
        /** Данные численных метрик */
        data: Any? = null,
        /** Объекты-параметры подставляемые в лог */
        objs: Map<String, Any>? = null,
    )

    fun error(
        msg: String = "",
        marker: String = "DEV",
        e: Throwable? = null,
        data: Any? = null,
        objs: Map<String, Any>? = null,
    ) = log(msg, LogLevel.ERROR, marker, e, data, objs)

    fun info(
        msg: String = "",
        marker: String = "DEV",
        data: Any? = null,
        objs: Map<String, Any>? = null,
    ) = log(msg, LogLevel.INFO, marker, null, data, objs)

    fun debug(
        msg: String = "",
        marker: String = "DEV",
        data: Any? = null,
        objs: Map<String, Any>? = null,
    ) = log(msg, LogLevel.DEBUG, marker, null, data, objs)

    /**
     * Выполняет переданный в [block]-е прикладной код с логированием до выполнения и после.
     */
    suspend fun <T> doWithLogging(
        id: String = "",
        level: LogLevel = LogLevel.INFO,
        block: suspend () -> T,
    ): T = try {
        log("Started $loggerId $id", level)
        val (result, diffTime) = measureTimedValue { block() }

        log(
            msg = "Finished $loggerId $id",
            level = level,
            objs = mapOf("metricHandleTime" to diffTime.toIsoString())
        )
        result
    } catch (e: Throwable) {
        log(
            msg = "Failed $loggerId $id",
            level = LogLevel.ERROR,
            e = e
        )
        throw e
    }

    /**
     * Выполняет переданный в [block]-е прикладной код с логированием ошибки.
     */
    suspend fun <T> doWithErrorLogging(
        id: String = "",
        throwRequired: Boolean = true,
        block: suspend () -> T,
    ): T? = try {
        val result = block()
        result
    } catch (e: Throwable) {
        log(
            msg = "Failed $loggerId $id",
            level = LogLevel.ERROR,
            e = e
        )
        if (throwRequired) throw e else null
    }

    override fun close() {}

    companion object {
        /** Логгер по умолчанию без идентификатора. */
        val DEFAULT = object: IKnthLogWrapper {
            override val loggerId: String = "NONE"

            override fun log(
                msg: String,
                level: LogLevel,
                marker: String,
                e: Throwable?,
                data: Any?,
                objs: Map<String, Any>?,
            ) {
                val markerString = marker
                    .takeIf { it.isNotBlank() }
                    ?.let { " ($it)" }
                val args = listOfNotNull(
                    "${Clock.System.now()} [${level.name}]$markerString: $msg",
                    e?.let { "${it.message ?: "Unknown reason"}:\n${it.stackTraceToString()}" },
                    data.toString(),
                    objs.toString(),
                )
                println(args.joinToString("\n"))
            }
        }
    }
}
