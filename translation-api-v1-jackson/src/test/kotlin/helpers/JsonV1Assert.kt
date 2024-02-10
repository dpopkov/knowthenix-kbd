package io.dpopkov.knowthenixkbd.api.v1.helpers

import kotlin.test.assertContains

/**
 * Вспомогательный класс для тестирования JSON строк.
 */
class JsonV1Assert(private val json: String) {
    /**
     * Проверяет, что тестируемая JSON строка содержит ожидаемое поле и значение.
     * @param name наименование поля
     * @param expected ожидаемое значение поля
     */
    fun field(name: String, expected: String) {
        assertContains(json, Regex("\"$name\":\\s*\"$expected\""),
            "JSON string should contain field '$name' with value '$expected'")
    }
}
