package com.github.phd.core.arrays

class Tensor4<T>(val dim1: Int, val dim2: Int, val dim3: Int, val dim4: Int, init: (Int, Int, Int, Int) -> T) {

    private val DIMENSIONS = com.github.phd.core.arrays.Dimensions(intArrayOf(dim1, dim2, dim3, dim4))

    private val data = MutableList(dim1 * dim2 * dim3 * dim4) { ijk ->
        val (i, j, k, l) = DIMENSIONS.decompose(ijk)
        init(i, j, k, l)
    }

    operator fun get(i: Int, j: Int, k: Int, l: Int) = data[DIMENSIONS.compose(i, j, k, l)]
    operator fun set(i: Int, j: Int, k: Int, l: Int, value: T) {
        data[DIMENSIONS.compose(i, j, k, l)] = value
    }

}