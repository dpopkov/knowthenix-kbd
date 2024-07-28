package io.dpopkov.knowthenixkbd.api.v1

import io.dpopkov.knowthenixkbd.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class RequestV1SerializationTest {
    private val request: IRequest = TranslationCreateRequest(
        debug = TranslationDebug(
            mode = TranslationRequestDebugMode.STUB,
            stub = TranslationRequestDebugStubs.BAD_LANGUAGE,
        ),
        translation = TranslationCreateObject(
            originalId = "original id",
            language = "ru",
            content = "translation content",
            syntax = SyntaxType.PLAIN_TEXT,
            trType = TranslationType.QUESTION,
            state = TranslationState.NEW,
            visibility = TranslationVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1RequestSerialize(request)

        println(json)

        json.assertContains("mode", "stub")
        json.assertContains("stub", "badLanguage")
        json.assertContains("originalId", "original id")
        json.assertContains("language", "ru")
        json.assertContains("content", "translation content")
        json.assertContains("syntax", "plainText")
        json.assertContains("state", "new")
        json.assertContains("visibility", "public")
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1RequestDeserialize<TranslationCreateRequest>(json)

        assertEquals(request, obj)
    }

    private fun String.assertContains(attribute: String, value: String) {
        kotlin.test.assertContains(this, Regex("\"$attribute\":\\s*\"$value\""))
    }
}
