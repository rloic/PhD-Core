package com.github.rloic.phd.core.arrays

import arrays.Dimensions

fun <T> tensor3OfNulls(dim1: Int, dim2: Int, dim3: Int): Tensor3<T?> = Tensor3(dim1, dim2, dim3) { _, _, _ -> null }
class Tensor3<T>(val dim1: Int, val dim2: Int, val dim3: Int, init: (Int, Int, Int) -> T) {

    private val DIMENSIONS = Dimensions(intArrayOf(dim1, dim2, dim3))

    private val data = MutableList(dim1 * dim2 * dim3) { ijk ->
        val (i, j, k) = DIMENSIONS.decompose(ijk)
        init(i, j, k)
    }

    operator fun get(i: Int) = Matrix(dim2, dim3) { j, k -> this[i, j, k] }
    operator fun get(i: Int, j: Int, k: Int) = data[DIMENSIONS.compose(i, j, k)]
    operator fun set(i: Int, j: Int, k: Int, value: T) {
        data[DIMENSIONS.compose(i, j, k)] = value
    }

    fun deepFlatten() = data.toMutableList()

}