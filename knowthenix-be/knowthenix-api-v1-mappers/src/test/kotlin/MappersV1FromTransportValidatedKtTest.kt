package io.dpopkov.knowthenixkbd.mappers.v1

import io.dpopkov.knowthenixkbd.api.v1.models.TranslationCreateRequest
import io.dpopkov.knowthenixkbd.api.v1.models.TranslationDebug
import io.dpopkov.knowthenixkbd.api.v1.models.TranslationRequestDebugStubs
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.stubs.KnthStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class MappersV1FromTransportValidatedKtTest {

    @Test
    fun fromTransportValidated() {
        val req = TranslationCreateRequest(
            debug = TranslationDebug(
                stub = TranslationRequestDebugStubs.SUCCESS
            )
        )

        val ctx = KnthContext()
        ctx.fromTransportValidated(req)

        assertEquals(KnthStubs.SUCCESS, ctx.stubCase)
    }
}
