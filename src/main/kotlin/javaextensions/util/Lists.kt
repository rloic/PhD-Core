package javaextensions.util

fun <T> List<T>.mapToIntArray(fn: (T) -> Int) = IntArray(size) { i -> fn(this[i]) }