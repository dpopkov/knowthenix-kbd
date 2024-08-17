package io.dpopkov.knowthenixkbd.cor.handlers

import io.dpopkov.knowthenixkbd.cor.TestContext
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CorWorkerTest {
    @Test
    fun `worker should execute handling function`() = runTest {
        val worker = CorWorker<TestContext>(
            title = "worker-1",
            blockHandle = { history += "test-1; "}
        )
        val ctx = TestContext()
        worker.exec(ctx)

        assertEquals("test-1; ", ctx.history)
    }

    @Test
    fun `worker should not execute when off`() = runTest {
        val worker = CorWorker<TestContext>(
            title = "worker-2",
            blockOn = { false },
            blockHandle = {
                history += "not-handled; "
            }
        )
        val ctx = TestContext()
        worker.exec(ctx)

        assertTrue(ctx.history.isEmpty())
    }

    @Test
    fun `worker should handle exception`() = runTest {
        val worker = CorWorker<TestContext>(
            title = "worker-3",
            blockHandle = {
                throw RuntimeException("test error")
            },
            blockExcept = { e ->
                history += e.message
            }
        )

        val ctx = TestContext()
        worker.exec(ctx)
        assertEquals("test error", ctx.history)
    }
}
