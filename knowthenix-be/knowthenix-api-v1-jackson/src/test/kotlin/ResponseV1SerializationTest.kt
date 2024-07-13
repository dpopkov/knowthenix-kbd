package io.dpopkov.knowthenixkbd.api.v1

import io.dpopkov.knowthenixkbd.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ResponseV1SerializationTest {
    private val response: IResponse = TranslationCreateResponse(
        responseType = "create",
        result = ResponseResult.SUCCESS,
        translation = TranslationResponseObject(
            originalId = "original id",
            language = "en",
            content = "translation content",
            syntax = SyntaxType.MARKDOWN,
            trType = TranslationType.ANSWER,
            state = TranslationState.NEW,
            visibility = TranslationVisibility.AUTHOR_ONLY,
            id = "translation id",
            ownerId = "owner id",
            lock = "lock id"
        )
    )

    @Test
    fun serialize() {
        val json = apiV1ResponseSerialize(response)

        json.assertContains("responseType", "create")
        json.assertContains("result", "success")
        json.assertContains("originalId", "original id")
        json.assertContains("language", "en")
        json.assertContains("content", "translation content")
        json.assertContains("syntax", "markdown")
        json.assertContains("trType", "answer")
        json.assertContains("state", "new")
        json.assertContains("visibility", "authorOnly")
        json.assertContains("id", "translation id")
        json.assertContains("ownerId", "owner id")
        json.assertContains("lock", "lock id")
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1ResponseDeserialize<TranslationCreateResponse>(json)

        assertEquals(response, obj)
    }

    private fun String.assertContains(attribute: String, value: String) {
        kotlin.test.assertContains(this, Regex("\"$attribute\":\\s*\"$value\""))
    }
}
