package io.dpopkov.knowthenixkbd.cor

data class TestContext(
    var status: CorStatuses = CorStatuses.NONE,
    var count: Int = 0,
    var history: String = "",
) {
    enum class CorStatuses {
        NONE,
        RUNNING,
        FAILING,
    }
}
