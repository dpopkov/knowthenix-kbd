package io.dpopkov.knowthenixkbd.app.rabbit.controller

import io.dpopkov.knowthenixkbd.app.rabbit.processor.RabbitProcessorBase
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicBoolean

private val log = LoggerFactory.getLogger(RabbitController::class.java)

class RabbitController(
    private val processors: Set<RabbitProcessorBase>
) : AutoCloseable {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val limitedParallelismContext = Dispatchers.IO.limitedParallelism(1)
    private val scope = CoroutineScope(Dispatchers.Default)
    private val runFlag = AtomicBoolean(true)

    fun start() {
        log.debug("Starting controller with ${processors.size} processors")
        processors.forEach {
            log.debug("Launching for ${it.processorConfig.consumerTag}")
            scope.launch(
                limitedParallelismContext + CoroutineName("thread-${it.processorConfig.consumerTag}")
            ) {
                while (runFlag.get()) {
                    try {
                        log.debug("Processing ${it.processorConfig.consumerTag}")
                        it.process()
                    }  catch (e: RuntimeException){
                        log.error("Error", e)
                    }
                }
            }
        }
    }

    private fun stop() {
        runFlag.set(false)
        processors.forEach { it.close() }
    }

    override fun close() {
        log.debug("Closing controller")
        stop()
        scope.cancel()
    }
}
