package com.github.rloic.phd.core.arrays

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

    fun <U> map(fn: (Int) -> U): Matrix<U> = Matrix(dim1, dim2) { i,j -> fn(this[i, j]) }

    override fun toString() = buildString {
        for (i in 0 until dim1) {
            for (j in 0 until dim2) {
                append(this@IntMatrix[i, j])
                append(", ")
            }
            if (dim2 != 0) {
                setLength(length - 1)
            }
            appendLine()
        }
    }
}