package com.github.rloic.phd.core.error

class ExplicitPanic(msg: String?, cause: Throwable?): Exception(msg, cause)
fun panic(msg: String? = null, cause: Throwable? = null): Nothing = throw ExplicitPanic(msg, cause)