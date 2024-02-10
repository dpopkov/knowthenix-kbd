package io.dpopkov.knowthenixkbd.api.v1

import io.dpopkov.knowthenixkbd.api.v1.helpers.JsonV1Assert
import io.dpopkov.knowthenixkbd.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ResponseV1SerializationTest {
    private val response: IResponse = TranslationCreateResponse(
        requestId = "123",
        result = ResponseResult.SUCCESS,
        translation = TranslationResponseObject(
            language = "translation language",
            formatSyntax = TranslationSyntax.PLAIN_TEXT,
            content = "translation content",
            state = TranslationState.EDITABLE,
            visibility = TranslationVisibility.PUBLIC,
            id = "456",
            ownerId = "owner id",
        )
    )

    @Test
    fun serialize() {
        val json = apiV1ResponseSerialize(response)

        val assert = JsonV1Assert(json)
        assert.field("responseType", "create")
        assert.field("requestId", "123")
        assert.field("result", "success")
        assert.field("language", "translation language")
        assert.field("formatSyntax", "plainText")
        assert.field("content", "translation content")
        assert.field("state", "editable")
        assert.field("visibility", "public")
        assert.field("id", "456")
        assert.field("ownerId", "owner id")
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1ResponseDeserialize<TranslationCreateResponse>(json)

        assertEquals(response, obj)
    }
}
