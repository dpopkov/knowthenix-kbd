import io.dpopkov.Greeting
import io.dpopkov.GreetingService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GreetingServiceJvmTest {
    @ParameterizedTest
    @CsvSource(value = [
        "World; Hello; Hello World in JVM!",
        "Мир; Здравствуй; Здравствуй Мир in JVM!",
    ], delimiter = ';')
    fun testJUnit5(name: String, message: String, expected: String) {
        val greeting = Greeting(name, message)
        val service = GreetingService()
        val result = service.format(greeting)
        assertEquals(expected, result)
    }
}