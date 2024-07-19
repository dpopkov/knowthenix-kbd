package io.dpopkov.knowthenixkbd.logging.jvm

import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test
import kotlin.test.assertTrue

class LoggerTest {
    private val logId = "test-logger"

    val lggr: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger("logger-name")

    data class Xx(val x: String = "sdf")

    @Test
    fun slf4jTest() {
        @Suppress("LoggingPlaceholderCountMatchesArgumentCount")
        lggr.info("Slf4j arguments: {} {} {}", 1, "sdf", Xx(), Xx("234234"))
    }

    @Test
    fun `logger init`() {
        val output = invokeLogger {
            println("Some action")
        }.toString()

        assertTrue(Regex("Started .* $logId.*").containsMatchIn(output))
        assertTrue(output.contains("Some action"))
        assertTrue(Regex("Finished .* $logId.*").containsMatchIn(output))
    }

    @Test
    fun `logger fails`() {
        val output = invokeLogger {
            throw RuntimeException("Some action")
        }.toString()

        assertTrue(Regex("Started .* $logId.*").containsMatchIn(output))
        assertTrue(Regex("Failed .* $logId.*").containsMatchIn(output))
    }

    private fun invokeLogger(block: suspend () -> Unit): ByteArrayOutputStream {
        val outputStreamCaptor = outputStreamCaptor()

        try {
            runBlocking {
                val logger = knthLoggerLogback(this::class)
                logger.doWithLogging(id = logId, block = block)
            }
        } catch (ignore: RuntimeException) {
        }

        return outputStreamCaptor
    }

    /** Байтовый массив, в который поступит текст направленный в system out */
    private fun outputStreamCaptor(): ByteArrayOutputStream {
        return ByteArrayOutputStream().apply {
            System.setOut(PrintStream(this))
        }
    }
}
