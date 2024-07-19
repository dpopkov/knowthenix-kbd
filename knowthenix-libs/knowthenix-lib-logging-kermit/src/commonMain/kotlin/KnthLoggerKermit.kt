package io.dpopkov.knowthenixkbd.logging.kermit

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.StaticConfig
import io.dpopkov.knowthenixkbd.logging.common.IKnthLogWrapper
import kotlin.reflect.KClass

@Suppress("unused")
fun mpLoggerKermit(loggerId: String): IKnthLogWrapper {
    val logger = Logger(
        config = StaticConfig(
            minSeverity = Severity.Info,
        ),
        tag = "DEV"
    )
    return KnthLoggerWrapperKermit(
        kermitLogger = logger,
        loggerId = loggerId,
    )
}

@Suppress("unused")
fun mpLoggerKermit(cls: KClass<*>): IKnthLogWrapper {
    val logger = Logger(
        config = StaticConfig(
            minSeverity = Severity.Info,
        ),
        tag = "DEV"
    )
    return KnthLoggerWrapperKermit(
        kermitLogger = logger,
        loggerId = cls.qualifiedName ?: "",
    )
}
