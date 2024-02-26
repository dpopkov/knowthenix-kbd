package io.dpopkov.knowthenixkbd.api.v2

import io.dpopkov.knowthenixkbd.api.v2.helpers.JsonV2Assert
import kotlinx.serialization.encodeToString
import io.dpopkov.knowthenixkbd.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class RequestV2SerializationTest {
    private val request: IRequest = TranslationCreateRequest(
        requestId = "123",
        debug = TranslationDebug(
            mode = TranslationRequestDebugMode.STUB,
            stub = TranslationRequestDebugStubs.BAD_LANGUAGE
        ),
        translation = TranslationCreateObject(
            language = "translation language",
            formatSyntax = TranslationSyntax.PLAIN_TEXT,
            content = "translation content",
            state = TranslationState.EDITABLE,
            questionId = "question id",
            visibility = TranslationVisibility.PUBLIC
        )
    )

    @Test
    fun serialize() {
        val json = apiV2RequestSerialize(request)

        val assert = JsonV2Assert(json)
        assert.field("requestType", "create")
        assert.field("requestId", "123")
        assert.field("mode", "stub")
        assert.field("stub", "badLanguage")
        assert.field("language", "translation language")
        assert.field("formatSyntax", "plainText")
        assert.field("content", "translation content")
        assert.field("state", "editable")
        assert.field("questionId", "question id")
        assert.field("visibility", "public")
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(request)
        val obj = apiV2RequestDeserialize<TranslationCreateRequest>(json)

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"requestId":"456"}
        """.trimIndent()
        val obj = apiV2Mapper.decodeFromString<TranslationCreateRequest>(jsonString)

        assertEquals("456", obj.requestId)
    }
}
