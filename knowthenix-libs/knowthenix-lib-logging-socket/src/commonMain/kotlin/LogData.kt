package io.dpopkov.knowthenixkbd.logging.socket

import kotlinx.serialization.Serializable
import io.dpopkov.knowthenixkbd.logging.common.LogLevel

@Serializable
data class LogData(
    val level: LogLevel,
    val message: String,
//    val data: T
)
