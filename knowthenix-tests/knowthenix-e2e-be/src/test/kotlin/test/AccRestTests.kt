package io.dpopkov.knowthenixkbd.e2e.be.test

import io.dpopkov.knowthenixkbd.e2e.be.docker.KtorJvmDockerCompose
import io.dpopkov.knowthenixkbd.e2e.be.docker.SpringDockerCompose
import io.dpopkov.knowthenixkbd.e2e.be.fixture.BaseFunSpec
import io.dpopkov.knowthenixkbd.e2e.be.fixture.client.RestClient
import io.dpopkov.knowthenixkbd.e2e.be.fixture.docker.DockerCompose
import io.dpopkov.knowthenixkbd.e2e.be.test.action.v1.toV1
import io.kotest.core.annotation.Ignored

enum class TestDebug {
    STUB, TEST, PROD
}

// Kotest не сможет подставить правильный аргумент конструктора, поэтому
// нужно запретить ему запускать этот класс
@Ignored
open class AccRestTestBase(dockerCompose: DockerCompose, debug: TestDebug) : BaseFunSpec(dockerCompose, {
    val restClient = RestClient(dockerCompose)
    testApiV1(restClient, prefix = "rest ", debug = debug.toV1())
})

@Suppress("unused")
class AccRestSpringTest : AccRestTestBase(SpringDockerCompose, debug = TestDebug.PROD)

@Suppress("unused")
class AccRestKtorJvmTest : AccRestTestBase(KtorJvmDockerCompose, debug = TestDebug.PROD)
