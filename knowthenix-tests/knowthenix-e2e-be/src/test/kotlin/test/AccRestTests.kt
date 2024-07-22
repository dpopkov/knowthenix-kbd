package io.dpopkov.knowthenixkbd.e2e.be.test

import io.dpopkov.knowthenixkbd.e2e.be.docker.SpringDockerCompose
import io.dpopkov.knowthenixkbd.e2e.be.fixture.BaseFunSpec
import io.dpopkov.knowthenixkbd.e2e.be.fixture.client.RestClient
import io.dpopkov.knowthenixkbd.e2e.be.fixture.docker.DockerCompose
import io.kotest.core.annotation.Ignored

// Kotest не сможет подставить правильный аргумент конструктора, поэтому
// нужно запретить ему запускать этот класс
@Ignored
open class AccRestTestBase(dockerCompose: DockerCompose) : BaseFunSpec(dockerCompose, {
    val restClient = RestClient(dockerCompose)
    testApiV1(restClient, "rest ")
})

@Suppress("unused")
class AccRestSpringTest : AccRestTestBase(SpringDockerCompose)
