package io.dpopkov.knowthenixkbd.api.v2

import io.dpopkov.knowthenixkbd.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlinx.serialization.encodeToString
import kotlin.test.assertNull

class RequestV2SerializationTest {
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
            aggregateId = "aggregate id",
            visibility = TranslationVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV2RequestSerialize(request)

        println(json)

        json.assertContains("mode", "stub")
        json.assertContains("stub", "badLanguage")
        json.assertContains("originalId", "original id")
        json.assertContains("language", "ru")
        json.assertContains("content", "translation content")
        json.assertContains("syntax", "plainText")
        json.assertContains("state", "new")
        json.assertContains("aggregateId", "aggregate id")
        json.assertContains("visibility", "public")
    }

    private fun String.assertContains(attribute: String, value: String) {
        assertContains(this, Regex("\"$attribute\":\\s*\"$value\""))
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(request)
//         val obj: TranslationCreateRequest = apiV2Mapper.decodeFromString<TranslationCreateRequest>(json)
        val obj = apiV2RequestDeserialize<TranslationCreateRequest>(json)

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNullTranslation() {
        val json = """
            {"translation": null}
        """.trimIndent()
        // val obj = apiV2RequestDeserialize<TranslationCreateRequest>(json)
        val obj = apiV2Mapper.decodeFromString<TranslationCreateRequest>(json)

        assertNull(obj.translation)
    }
}
