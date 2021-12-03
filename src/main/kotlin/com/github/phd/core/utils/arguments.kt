package com.github.phd.core.utils

import java.lang.RuntimeException

fun parseArgs(args: Array<String>, delimiter: String = "=") = args.map {
    val idx = it.indexOf(delimiter)
    val key = it.substring(0, idx)
    val value = it.substring(idx + delimiter.length)
    Pair(key, value)
}.toMap().toMutableMap()

class MissingArgumentException (argName: String): RuntimeException("Argument $argName is missing.")

fun <T> Map<String, T>.expectArgument(name: String) = this[name] ?: throw MissingArgumentException(name)