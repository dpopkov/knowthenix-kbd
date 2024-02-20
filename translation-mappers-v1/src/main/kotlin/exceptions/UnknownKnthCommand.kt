package io.dpopkov.knowthenixkbd.mappers.v1.exceptions

import io.dpopkov.knowthenixkbd.common.models.KnthCommand

class UnknownKnthCommand(command: KnthCommand) :
    RuntimeException("Unknown command '$command' at mapping toTransport stage") {
}
