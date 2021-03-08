package com.github.rloic.phd.core.arrays

import arrays.Dimensions

fun IntTensor3(dim1: Int, dim2: Int, dim3: Int) = IntTensor3(dim1, dim2, dim3) { _, _, _ -> 0 }
class IntTensor3(val dim1: Int, val dim2: Int, val dim3: Int, init: (Int, Int, Int) -> Int) {

    private val DIMENSIONS = Dimensions(intArrayOf(dim1, dim2, dim3))

    private val data = IntArray(dim1 * dim2 * dim3) { ijk ->
        val (i, j, k) = DIMENSIONS.decompose(ijk)
        init(i, j, k)
    }

    operator fun get(i: Int) = IntMatrix(dim2, dim3) { j, k -> this[i, j, k] }
    operator fun get(i: Int, j: Int, k: Int) = data[DIMENSIONS.compose(i, j, k)]
    operator fun set(i: Int, value: IntMatrix) {
        for (j in 0 until dim2) {
            for (k in 0 until dim3) {
                data[DIMENSIONS.compose(i, j, k)] = value[j, k]
            }
        }
    }

    operator fun set(i: Int, j: Int, k: Int, value: Int) {
        data[DIMENSIONS.compose(i, j, k)] = value
    }

    fun deepFlatten() = data.copyOf()
    fun sum() = data.sum()
    fun toArrays() = Array(dim1) { i -> Array(dim2) { j -> IntArray(dim3) { k -> this[i, j, k] } } }

    override fun toString() = buildString {
        for (j in 0 until dim2) {
            for (i in 0 until dim1) {
                for (k in 0 until dim3) {
                    append(this@IntTensor3[i, j, k])
                    append(", ")
                }
                if (dim3 != 0) {
                    setLength(length - 1)
                }
                append('\t')
            }
            if (dim1 != 0) {
                setLength(length - 1)
            }
            appendLine()
        }
    }

}