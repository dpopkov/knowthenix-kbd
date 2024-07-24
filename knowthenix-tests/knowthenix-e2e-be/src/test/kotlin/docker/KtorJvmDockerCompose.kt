package io.dpopkov.knowthenixkbd.e2e.be.docker

import io.dpopkov.knowthenixkbd.e2e.be.fixture.docker.AbstractDockerCompose

object KtorJvmDockerCompose : AbstractDockerCompose(
    "app-ktor-jvm_1", 8080, "docker-compose-ktor-jvm.yml"
)
