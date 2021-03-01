package com.github.rloic.phd.core.arrays

import arrays.Dimensions

class Tensor5<T>(
    val dim1: Int,
    val dim2: Int,
    val dim3: Int,
    val dim4: Int,
    val dim5: Int,
    init: (Int, Int, Int, Int, Int) -> T
) {

    private val DIMENSIONS = Dimensions(intArrayOf(dim1, dim2, dim3, dim4, dim5))

    private val data = MutableList(dim1 * dim2 * dim3 * dim4 * dim5) { ijk ->
        val (i, j, k, l, m) = DIMENSIONS.decompose(ijk)
        init(i, j, k, l, m)
    }

    operator fun get(i: Int, j: Int, k: Int, l: Int, m: Int) = data[DIMENSIONS.compose(i, j, k, l, m)]
    operator fun set(i: Int, j: Int, k: Int, l: Int, m: Int, value: T) {
        data[DIMENSIONS.compose(i, j, k, l, m)] = value
    }

}