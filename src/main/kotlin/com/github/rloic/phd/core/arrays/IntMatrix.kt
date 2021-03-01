package com.github.rloic.phd.core.arrays

import arrays.Dimensions

fun IntMatrix(dim1: Int, dim2: Int) = IntMatrix(dim1, dim2) { _, _ -> 0 }
class IntMatrix(val dim1: Int, val dim2: Int, init: (Int, Int) -> Int) {

    private val DIMENSIONS = Dimensions(intArrayOf(dim1, dim2))

    private val data = IntArray(dim1 * dim2) { ij ->
        val (i, j) = DIMENSIONS.decompose(ij)
        init(i, j)
    }

    operator fun get(i: Int, j: Int) = data[DIMENSIONS.compose(i, j)]
    operator fun set(i: Int, j: Int, value: Int) {
        data[DIMENSIONS.compose(i, j)] = value
    }

    fun sum() = data.sum()
    fun deepFlatten() = data.copyOf()
    fun toArrays() = Array(dim1) { i -> Array(dim2) { j -> this[i, j] } }

}