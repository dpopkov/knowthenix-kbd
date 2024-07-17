import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldContainOnlyDigits

class UUIDTestCommon : FunSpec() {
    init {
        test("date should start with year") {
            println(currentDate().iso)
            currentDate().iso
                .take(4)
                .shouldContainOnlyDigits()
        }

        test("date should contain separator") {
            currentDate().iso shouldContain "T"
        }

        test("date should contain date and time") {
            val data = currentDate().iso
                .split("T")

            //simple checks
            data.size shouldBe 2

            data.first().filter {
                it == '-'
            }.length shouldBe 2
            data.last().filter {
                it == ':'
            } shouldBe "::"
        }
    }
}
