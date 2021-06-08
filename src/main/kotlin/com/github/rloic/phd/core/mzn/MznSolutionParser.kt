package com.github.rloic.phd.core.mzn

import com.github.rloic.phd.core.utils.Parser

interface MznSolutionParser<T>: Parser<MznSolution, T> {


    fun <T> List<T>.mapToInt(fn: (T) -> Int) = IntArray(size) { fn(this[it]) }

    fun toLinear(cells: String) = cells
        .substringAfter('[')
        .substringBeforeLast(']')
        .split(',')
        .filterNot { it.isBlank() }
        .mapToInt { if (it.equals("true", false) || it == "1") 1 else 0 }

    fun String.parseObjective() = substringAfter("= ").substringBeforeLast(';').toInt()

    fun List<String>.getIntLinear(key: String) = toLinear(first { it.startsWith(key) })

    fun List<String>.getIntValue(key: String) = first { it.startsWith(key) }.parseObjective()

}