package io.dpopkov.knowthenixkbd.e2e.be.docker

import io.dpopkov.knowthenixkbd.e2e.be.fixture.docker.AbstractDockerCompose

object WiremockDockerCompose : AbstractDockerCompose(
    "app-wiremock_1", 8080, "docker-compose-wiremock.yml"
)
