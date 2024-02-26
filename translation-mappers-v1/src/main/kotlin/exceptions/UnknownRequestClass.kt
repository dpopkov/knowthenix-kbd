package io.dpopkov.knowthenixkbd.mappers.v1.exceptions

class UnknownRequestClass(clazz: Class<*>): RuntimeException("Class $clazz cannot be mapped to KnthContext")
