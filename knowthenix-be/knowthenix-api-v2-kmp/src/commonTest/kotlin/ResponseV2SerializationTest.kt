package io.dpopkov.knowthenixkbd.api.v2

import io.dpopkov.knowthenixkbd.api.v2.models.*
import kotlinx.serialization.encodeToString
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV2SerializationTest {
    private val response: IResponse = TranslationCreateResponse(
        result = ResponseResult.SUCCESS,
        translation = TranslationResponseObject(
            originalId = "original id",
            language = "en",
            content = "content",
            syntax = SyntaxType.PLAIN_TEXT,
            trType = TranslationType.QUESTION,
            state = TranslationState.NEW,
            aggregateId = "aggregate id",
            visibility = TranslationVisibility.PUBLIC,
            id = "translation id",
            ownerId = "owner id",
            lock = "translation lock",
            permissions = setOf(TranslationPermissions.READ, TranslationPermissions.UPDATE)
        )
    )

    @Test
    fun serialize() {
        val json = apiV2ResponseSerialize(response)
        println(json)

        json.assertContains("result", "success")
        json.assertContains("originalId", "original id")
        json.assertContains("language", "en")
        json.assertContains("content", "content")
        json.assertContains("syntax", "plainText")
        json.assertContains("trType", "question")
        json.assertContains("state", "new")
        json.assertContains("aggregateId", "aggregate id")
        json.assertContains("visibility", "public")
        json.assertContains("id", "translation id")
        json.assertContains("ownerId", "owner id")
        json.assertContains("lock", "translation lock")
        json.assertContains("id", "translation id")
        json.assertContains("permissions", arrayOf("read", "update"))
    }

    private fun String.assertContains(attribute: String, value: String) {
        assertContains(this, Regex("\"$attribute\":\\s*\"$value\""))
    }

    private fun String.assertContains(attribute: String, array: Array<String>) {
        val items = array.joinToString(separator = "\",\"", prefix = "\\[\"", postfix = "\"\\]")
        assertContains(this, Regex("\"$attribute\":\\s*$items"))
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(response)
        val obj = apiV2ResponseDeserialize<TranslationCreateResponse>(json)

        assertEquals(response, obj)
    }
}
