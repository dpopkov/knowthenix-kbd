package io.dpopkov.knowthenixkbd.cor.handlers

import io.dpopkov.knowthenixkbd.cor.TestContext
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CorChainTest {
    @Test
    fun `chain should execute workers`() = runTest {
        val titles = listOf("worker-1", "worker-2", "worker-3")
        val chain: CorChain<TestContext> = CorChain(
            title = "test chain",
            corExecs = titles.map { createWorker(it) },
        )
        val ctx = TestContext()
        chain.exec(ctx)

        assertEquals("worker-1; worker-2; worker-3; ", ctx.history)
    }

    private fun createWorker(title: String) = CorWorker<TestContext>(
        title = title,
        blockHandle = {
            history += "$title; "
        }
    )
}
