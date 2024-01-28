import io.dpopkov.Greeting
import io.dpopkov.GreetingService
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertTrue

class GreetingServiceTest {
    @Test
    fun testServiceFormat() {
        val greeting = Greeting("Test", "Hello")
        val service = GreetingService()
        val result = service.format(greeting)
        assertContains(result, "Hello Test")
        assertTrue {
            result.contains("JVM") || result.contains("native")
        }
    }
}
