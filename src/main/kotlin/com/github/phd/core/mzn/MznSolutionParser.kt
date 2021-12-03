package com.github.phd.core.mzn

import com.github.phd.core.utils.Parser

interface MznSolutionParser<T>: Parser<MznSolution, T> {


    fun <T> List<T>.mapToInt(fn: (T) -> Int) = IntArray(size) { fn(this[it]) }

    fun toLinear(cells: String) = cells
        .substringAfter('[')
        .substringBeforeLast(']')
        .split(',')
        .filterNot { it.isBlank() }
        .mapToInt { if (it.equals("true", false) || it == "1") 1 else 0 }

    fun String.parseInt() = substringAfter("= ").substringBeforeLast(';').toInt()
    fun String.parseBool() = substringAfter("= ").substringBeforeLast(';').toBoolean()

    fun List<String>.getIntArray(key: String) = toLinear(first { it.startsWith(key) })

    fun List<String>.getIntValue(key: String) = first { it.startsWith(key) }.parseInt()
    fun List<String>.getIntValueOr(key: String, def: Int) = firstOrNull { it.startsWith(key) }?.parseInt() ?: def
    fun List<String>.getBoolValue(key: String) = first { it.startsWith(key) }.parseBool()

}