package com.github.rloic.phd.core.arrays

class Dimensions(private val dims: IntArray) {
    fun decompose(idx: Int): IntArray {
        var tmp = idx
        val indices = IntArray(dims.size)

        var i = dims.size - 1
        while (i >= 0) {
            val dim = dims[i]
            val index = tmp % dim
            tmp = (tmp - index) / dim
            indices[i] = index
            i -= 1
        }

        return indices
    }

    fun compose(vararg indices: Int): Int {
        assert(indices.size == dims.size)

        var index = indices[0]
        for (i in 1 until indices.size) {
            index *= dims[i]
            index += indices[i]
        }

        return index
    }
}