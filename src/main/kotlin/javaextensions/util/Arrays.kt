package javaextensions.util

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.arrays.IntTensor3

fun IntArray.reshape(dim1: Int, dim2: Int) = IntMatrix(dim1, dim2) { i, j -> this[i * dim2 + j] }
fun IntArray.reshape(dim1: Int, dim2: Int, dim3: Int) = IntTensor3(dim1, dim2, dim3) { i, j, k -> this[((i * dim3) + j) * dim2 + k] }

fun IntArray.mapToBool(fn: (Int) -> Boolean = { index -> this[index] != 0 }) = BooleanArray(size, fn)