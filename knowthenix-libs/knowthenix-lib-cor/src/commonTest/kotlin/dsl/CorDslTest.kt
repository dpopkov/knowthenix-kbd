package io.dpopkov.knowthenixkbd.cor.dsl

import io.dpopkov.knowthenixkbd.cor.ICorExec
import io.dpopkov.knowthenixkbd.cor.TestContext
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CorDslTest {
    @Test
    fun `handle should execute`() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                handle {
                    history += "worker-1"
                }
            }
        }.build()
        val ctx = chain.execWithTestContext()

        assertEquals("worker-1", ctx.history)
    }

    @Test
    fun `on should check condition`() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                on { false }
                handle { history += "worker-1; " }
            }
            worker {
                on { true }
                handle {
                    history += "worker-2; "
                    status = TestContext.CorStatuses.FAILING
                }
            }
            worker {
                on { status == TestContext.CorStatuses.FAILING }
                handle { history += "worker-3; " }
            }
        }.build()
        val ctx = chain.execWithTestContext()

        assertEquals("worker-2; worker-3; ", ctx.history)
    }

    @Test
    fun `except should execute when exception`() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                handle { throw RuntimeException("test error") }
                except { history += it.message }
            }
        }.build()
        val ctx = chain.execWithTestContext()

        assertEquals("test error", ctx.history)
    }

    @Test
    fun `should throw when exception and no except`() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                handle { throw RuntimeException("test error") }
            }
        }.build()
        val exception = assertFails {
            chain.execWithTestContext()
        }
        assertEquals("test error", exception.message)
    }

    @Test
    fun `complex chain example`() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                title = "Инициализация статуса"
                description = "При старте обработки цепочки, статус еще не установлен. Проверяем его."
                on { status == TestContext.CorStatuses.NONE }
                handle {
                    status = TestContext.CorStatuses.RUNNING
                    history += "CorStatuses.RUNNING; "
                }
            }

            chain {
                title = "Chain-1"
                on { status == TestContext.CorStatuses.RUNNING }
                worker {
                    title = "Лямбда обработчик"
                    description = "Пример использования обработчика в виде лямбды"
                    handle {
                        count += 4
                        history += "count += 4; "
                    }
                }
                chain {
                    title = "Chain-1.1"
                    on { status == TestContext.CorStatuses.RUNNING }
                    worker {
                        handle {
                            count = (count * count) - 1
                            history += "count = (count * count) - 1; "
                        }
                    }
                }
            }

            worker {
                on { status == TestContext.CorStatuses.RUNNING }
                handle {
                    status = TestContext.CorStatuses.NONE
                    history += "CorStatuses.NONE; "
                }
            }
        }.build()

        val ctx = chain.execWithTestContext()
        println("Complete: $ctx")

        assertEquals(15, ctx.count)
        assertEquals(TestContext.CorStatuses.NONE, ctx.status)
        assertEquals(
            "CorStatuses.RUNNING; count += 4; count = (count * count) - 1; CorStatuses.NONE; ",
            ctx.history
        )
    }

    private suspend fun ICorExec<TestContext>.execWithTestContext(): TestContext {
        return TestContext().also {
            exec(it)
        }
    }
}
