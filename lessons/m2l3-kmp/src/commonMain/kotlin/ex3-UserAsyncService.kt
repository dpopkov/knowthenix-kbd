package io.dpopkov.knowthenixkbd.m2l3

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class UserAsyncService() {
    suspend fun serve(user: User): Pair<String, User>
}
