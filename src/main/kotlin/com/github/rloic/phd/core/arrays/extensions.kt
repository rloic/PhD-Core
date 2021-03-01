package com.github.rloic.phd.core.arrays

fun IntArray.reshape(dim1: Int, dim2: Int) = IntMatrix(dim1, dim2) { i, j -> this[i * dim2 + j] }
fun IntArray.reshape(dim1: Int, dim2: Int, dim3: Int) = IntTensor3(dim1, dim2, dim3) { i, j, k -> this[((i * dim3) + j) * dim2 + k] }