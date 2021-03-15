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
    operator fun get(i: Int) = List(dim2) { j -> this[i, j] }
    operator fun set(i: Int, j: Int, value: T) {
        data[DIMENSIONS.compose(i, j)] = value
    }

    fun deepFlatten() = data.toMutableList()

    fun <U> map(fn: (T) -> U) = Matrix(dim1, dim2) { j, k -> fn(this[j, k]) }

    override fun toString() = buildString {
        for (i in 0 until dim1) {
            for (j in 0 until dim2) {
                append(this@Matrix[i, j])
                append(", ")
            }
            if (dim2 != 0) {
                setLength(length - 1)
            }
            appendLine()
        }
    }

}

fun <T> Matrix<T?>.filled(): Matrix<T> = Matrix(dim1, dim2) { i, j -> this[i, j]!! }

fun <T> List<Matrix<T>>.matcheDims() = if (size == 0) true else all { it.dim1 == this[0].dim1 && it.dim2 == this[0].dim2 }