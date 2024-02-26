package io.dpopkov.knowthenixkbd.api.v2

import io.dpopkov.knowthenixkbd.api.v2.models.IRequest
import io.dpopkov.knowthenixkbd.api.v2.models.IResponse
import kotlinx.serialization.json.Json

val apiV2Mapper = Json {
    ignoreUnknownKeys = true
}

fun apiV2RequestSerialize(obj: IRequest): String =
    apiV2Mapper.encodeToString(IRequest.serializer(), obj)

@Suppress("UNCHECKED_CAST")
fun <T : IRequest> apiV2RequestDeserialize(json: String): T =
    apiV2Mapper.decodeFromString<IRequest>(json) as T

fun apiV2ResponseSerialize(obj: IResponse): String =
    apiV2Mapper.encodeToString(IResponse.serializer(), obj)

@Suppress("UNCHECKED_CAST")
fun <T : IResponse> apiV2ResponseDeserialize(json: String): T =
    apiV2Mapper.decodeFromString<IResponse>(json) as T
