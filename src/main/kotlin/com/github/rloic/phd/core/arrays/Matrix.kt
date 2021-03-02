package com.github.rloic.phd.core.arrays

import arrays.Dimensions

fun <T> matrixOfNulls(dim1: Int, dim2: Int): Matrix<T?> = Matrix(dim1, dim2) { _, _ -> null }
class Matrix<T>(val dim1: Int, val dim2: Int, init: (Int, Int) -> T) {

    private val DIMENSIONS = Dimensions(intArrayOf(dim1, dim2))

    private val data = MutableList(dim1 * dim2) { ij ->
        val (i, j) = DIMENSIONS.decompose(ij)
        init(i, j)
    }

    operator fun get(i: Int, j: Int) = data[DIMENSIONS.compose(i, j)]
    operator fun set(i: Int, j: Int, value: T) {
        data[DIMENSIONS.compose(i, j)] = value
    }

    fun deepFlatten() = data.toMutableList()

}