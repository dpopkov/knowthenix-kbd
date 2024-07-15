package io.dpopkov.knowthenixkbd.mappers.v1.exceptions

class UnknownRequestClass(clazz: Class<*>) :
    RuntimeException("Class $clazz is unknown and cannot be mapped to a specific request class")
