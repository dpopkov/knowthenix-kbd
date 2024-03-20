package io.dpopkov.knowthenixkbd.api.v1

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import io.dpopkov.knowthenixkbd.api.v1.models.IRequest
import io.dpopkov.knowthenixkbd.api.v1.models.IResponse

val apiV1Mapper: JsonMapper = JsonMapper.builder().run {
    enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
    build()
}

fun apiV1RequestSerialize(request: IRequest): String =
    apiV1Mapper.writeValueAsString(request)

@Suppress("UNCHECKED_CAST")
fun <T: IRequest> apiV1RequestDeserialize(json: String): T =
    apiV1Mapper.readValue(json, IRequest::class.java) as T

fun <T: IRequest> apiV1RequestDeserialize(bytes: ByteArray): T =
    apiV1Mapper.readValue(bytes, IRequest::class.java) as T

fun apiV1ResponseSerialize(response: IResponse): String =
    apiV1Mapper.writeValueAsString(response)

fun apiV1ResponseSerializeAsBytes(response: IResponse): ByteArray =
    apiV1Mapper.writeValueAsBytes(response)

@Suppress("UNCHECKED_CAST")
fun <T: IResponse> apiV1ResponseDeserialize(json: String): T =
    apiV1Mapper.readValue(json, IResponse::class.java) as T
