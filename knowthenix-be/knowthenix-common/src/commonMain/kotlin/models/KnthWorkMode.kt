package io.dpopkov.knowthenixkbd.common.models

/**
 * Перечисление режимов работы приложения, позволяющее приложению без изменения настроек
 * обрабатывать запросы разных видов (для конечных пользователей, тестировщиков и т.п).
 */
enum class KnthWorkMode {
    /**
     * Реальный production режим.
     */
    PROD,
    /**
     * Тестовый режим, в котором используется тестовая база данных.
     */
    TEST,
    /**
     * Режим стабов, при котором база данных не испльзуется,
     * а будет возвращен фиксированный стаб, который задается перечислением KnthStubs.
     */
    STUB,
}
