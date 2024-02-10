package io.dpopkov.knowthenixkbd.api.v2

import io.dpopkov.knowthenixkbd.api.v2.helpers.JsonV2Assert
import kotlinx.serialization.encodeToString
import io.dpopkov.knowthenixkbd.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ResponseV2SerializationTest {
    private val response: IResponse = TranslationCreateResponse(
        requestId = "123",
        result = ResponseResult.SUCCESS,
        translation = TranslationResponseObject(
            language = "translation language",
            formatSyntax = TranslationSyntax.PLAIN_TEXT,
            content = "translation content",
            state = TranslationState.EDITABLE,
            questionId = "question id",
            visibility = TranslationVisibility.PUBLIC,
            id = "456",
            ownerId = "owner id",
        )
    )

    @Test
    fun serialize() {
        val json = apiV2ResponseSerialize(response)

        val assert = JsonV2Assert(json)
        assert.field("responseType", "create")
        assert.field("requestId", "123")
        assert.field("result", "success")
        assert.field("language", "translation language")
        assert.field("formatSyntax", "plainText")
        assert.field("content", "translation content")
        assert.field("state", "editable")
        assert.field("questionId", "question id")
        assert.field("visibility", "public")
        assert.field("id", "456")
        assert.field("ownerId", "owner id")
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(response)
        val obj = apiV2ResponseDeserialize<TranslationCreateResponse>(json)

        assertEquals(response, obj)
    }
}
