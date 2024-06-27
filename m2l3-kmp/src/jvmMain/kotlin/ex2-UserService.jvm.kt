package io.dpopkov.knowthenixkbd.m2l3

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class UserService {
    actual fun serve(user: User) = "JVM Service for User $user"
}