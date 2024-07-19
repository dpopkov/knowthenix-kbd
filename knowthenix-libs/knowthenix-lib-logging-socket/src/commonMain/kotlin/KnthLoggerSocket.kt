package io.dpopkov.knowthenixkbd.logging.socket

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import io.dpopkov.knowthenixkbd.logging.common.IKnthLogWrapper
import kotlin.reflect.KClass

data class SocketLoggerSettings(
    val host: String = "127.0.0.1",
    val port: Int = 9002,
    val emitToStdout: Boolean = true,
    val bufferSize: Int = 16,
    val overflowPolicy: BufferOverflow = BufferOverflow.SUSPEND,
    val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + CoroutineName("Logging")),
)

@Suppress("unused")
fun knthLoggerSocket(
    loggerId: String,
    settings: SocketLoggerSettings = SocketLoggerSettings()
): IKnthLogWrapper = KnthLoggerWrapperSocket(
    loggerId = loggerId,
    host = settings.host,
    port = settings.port,
    emitToStdout = settings.emitToStdout,
    bufferSize = settings.bufferSize,
    overflowPolicy = settings.overflowPolicy,
    scope = settings.scope,
)

@Suppress("unused")
fun knthLoggerSocket(cls: KClass<*>, settings: SocketLoggerSettings = SocketLoggerSettings()): IKnthLogWrapper =
    knthLoggerSocket(
        loggerId = cls.qualifiedName ?: "",
        settings = settings,
    )
