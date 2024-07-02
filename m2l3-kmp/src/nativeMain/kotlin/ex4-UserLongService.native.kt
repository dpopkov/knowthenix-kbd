package io.dpopkov.knowthenixkbd.m2l3

import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class UserLongService actual constructor() {
    actual suspend fun serve(user: User): Pair<String, User> {
        delay(3.seconds)
        return "Native" to user
    }
}
