package io.dpopkov.knowthenixkbd.common.repo

import io.dpopkov.knowthenixkbd.common.helpers.errorSystem

abstract class TranslationRepoBase : IRepoTranslation {
    protected suspend fun tryTranslationMethod(block: suspend () -> IDbTranslationResponse): IDbTranslationResponse =
        try {
            block()
        } catch (e: Throwable) {
            DbTranslationResponseErr(
                errorSystem(
                    violationCode = "methodException",
                    e = e
                )
            )
        }

    protected suspend fun tryTranslationsMethod(block: suspend () -> IDbTranslationsResponse): IDbTranslationsResponse =
        try {
            block()
        } catch (e: Throwable) {
            DbTranslationsResponseErr(
                errorSystem(
                    violationCode = "methodException",
                    e = e
                )
            )
        }
}
