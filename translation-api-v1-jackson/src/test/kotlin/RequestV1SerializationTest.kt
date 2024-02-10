package io.dpopkov.knowthenixkbd.api.v1

import io.dpopkov.knowthenixkbd.api.v1.helpers.JsonV1Assert
import io.dpopkov.knowthenixkbd.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class RequestV1SerializationTest {
    private val request = TranslationCreateRequest(
        requestId = "123",
        debug = TranslationDebug(
            mode = TranslationRequestDebugMode.STUB,
            stub = TranslationRequestDebugStubs.BAD_LANGUAGE,
        ),
        translation = TranslationCreateObject(
            language = "translation language",
            formatSyntax = TranslationSyntax.PLAIN_TEXT,
            content = "translation content",
            state = TranslationState.EDITABLE,
            visibility = TranslationVisibility.PUBLIC,
        ),
    )

    @Test
    fun serialize() {
        val json = apiV1RequestSerialize(request)

        val assert = JsonV1Assert(json)
        assert.field("requestType", "create")
        assert.field("requestId", "123")
        assert.field("mode", "stub")
        assert.field("stub", "badLanguage")
        assert.field("language", "translation language")
        assert.field("formatSyntax", "plainText")
        assert.field("content", "translation content")
        assert.field("state", "editable")
        assert.field("visibility", "public")
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1RequestDeserialize<TranslationCreateRequest>(json)

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"requestId": "456"}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, TranslationCreateRequest::class.java)

        assertEquals("456", obj.requestId)
    }
}
