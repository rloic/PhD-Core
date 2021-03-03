package com.github.rloic.phd.core.arrays

import arrays.Dimensions

fun <T> tensor3OfNulls(dim1: Int, dim2: Int, dim3: Int): Tensor3<T?> = Tensor3(dim1, dim2, dim3) { _, _, _ -> null }
fun <T> Tensor3(dim1: Int, init: (Int) -> Matrix<T>): Tensor3<T> {
    val matrices = mutableListOf<Matrix<T>>()
    for (i in 0 until dim1) {
        matrices += init(i)
    }

    val dim2 = matrices.maxOfOrNull(Matrix<T>::dim1) ?: 0
    val dim3 = matrices.maxOfOrNull(Matrix<T>::dim2) ?: 0

    return Tensor3(dim1, dim2, dim3) { i, j, k -> matrices[i][j, k] }
}
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
    operator fun set(i: Int, value: Matrix<T>) {
        for (j in 0 until dim2) {
            for (k in 0 until dim3) {
                data[DIMENSIONS.compose(i, j, k)] = value[j, k]
            }
        }
    }

    fun deepFlatten() = data.toMutableList()

}