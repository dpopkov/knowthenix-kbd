package io.dpopkov.knowthenixkbd.e2e.be.test.action.v1

import co.touchlab.kermit.Logger
import io.dpopkov.knowthenixkbd.api.v1.apiV1RequestSerialize
import io.dpopkov.knowthenixkbd.api.v1.apiV1ResponseDeserialize
import io.dpopkov.knowthenixkbd.api.v1.models.IRequest
import io.dpopkov.knowthenixkbd.api.v1.models.IResponse
import io.dpopkov.knowthenixkbd.e2e.be.fixture.client.Client

private val log = Logger

suspend fun Client.sendAndReceive(path: String, request: IRequest): IResponse {
    val requestBody = apiV1RequestSerialize(request)
    log.i { "Send to v1/$path\n$requestBody" }

    val responseBody = sendAndReceive("v1", path, requestBody)
    log.i { "Received\n$responseBody" }

    return apiV1ResponseDeserialize(responseBody)
}
