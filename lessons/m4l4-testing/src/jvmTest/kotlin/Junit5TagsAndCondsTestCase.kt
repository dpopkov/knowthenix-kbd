import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.*
import kotlin.math.log2
import kotlin.test.Ignore
import kotlin.test.assertEquals


@Tags(
    Tag("junit"),
    Tag("sampling")
)
internal class Junit5TagsTestCase {
    @Test
    fun `Log to base 2 of 8 should be equal to 3`() {
        Assertions.assertEquals(3.0, log2(8.0))
    }
}

internal class Junit5ConditionsTestCase {

    @Test
    @Ignore("disabled")
    fun `not working rn`() = doTest(8, 11, 19)

    @Test
    @EnabledOnOs(OS.MAC, disabledReason = "Not supported")
    fun `mac only`() = doTest(55, 33, 88)

    @Test
    @EnabledOnOs(OS.LINUX, disabledReason = "Not supported")
    fun `linux only`() = doTest(11, 22, 33)

    @Test
    @EnabledOnOs(OS.WINDOWS, disabledReason = "Not supported")
    fun `windows only`() = doTest(55, 33, 88)

    @Test
    @EnabledForJreRange(min = JRE.JAVA_8, max = JRE.JAVA_11)
    fun `java 8-11 only`() = doTest(8, 11, 19)

    @Test
    @EnabledForJreRange(min = JRE.JAVA_11, max = JRE.JAVA_17)
    fun `java 11-17 only`() = doTest(8, 11, 19)

    @Test
    @EnabledIfEnvironmentVariable(named = "HOME", matches = "/Users/Jane.Doe")
    fun `env test`() = doTest(8, 11, 19)

    @Test
    @EnabledIfSystemProperty(named = "java.home", matches = "/Users/Jane.Doe")
    fun `sys prop test`() = doTest(8, 11, 19)

    private fun doTest(a: Int, b: Int, expected: Int) {
        val res = a + b
        assertEquals(expected, res)
    }
}
