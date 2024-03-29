package javaextensions.util

import com.github.phd.core.arrays.IntMatrix
import com.github.phd.core.arrays.IntTensor3
import java.io.File
import java.lang.Integer.parseInt

private fun parseIntDynamic(s: String) = when {
    s.startsWith("0x") -> parseInt(s.substring(2), 16)
    s.startsWith("0o") -> parseInt(s.substring(2), 8)
    s.startsWith("0b") -> parseInt(s.substring(2), 2)
    else -> parseInt(s)
}

fun intArrayFrom(file: File): IntArray = file.readText()
    .replace("\\s".toRegex(), "")
    .split(',')
    .filterNot(String::isNullOrBlank)
    .map(::parseIntDynamic)
    .toIntArray()

fun IntArray.reshape(dim1: Int, dim2: Int): IntMatrix {
    check(dim1 * dim2 == size) { "IntArray of size $size cannot be reshaped to matrix of dims: [$dim1, $dim2]" }
    var cpt = 0
    return IntMatrix(dim1, dim2) { _, _ -> this[cpt++] }
}

fun IntArray.reshape(dim1: Int, dim2: Int, dim3: Int): IntTensor3 {
    check(dim1 * dim2 * dim3 == size) { "IntArray of size $size cannot be reshaped to matrix of dims: [$dim1, $dim2, $dim3]" }
    var cpt = 0
    return IntTensor3(dim1, dim2, dim3) { _, _, _ -> this[cpt++] }
}

fun IntArray.mapToBool(fn: (Int) -> Boolean = { index -> this[index] != 0 }) = BooleanArray(size, fn)