package io.dpopkov.knowthenixkbd.common.exceptions

import io.dpopkov.knowthenixkbd.common.models.KnthCommand

class UnknownKnthCommand(command: KnthCommand) : Throwable("Wrong command $command at mapping stage")
