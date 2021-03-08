package javaextensions.util

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.arrays.IntTensor3

fun IntArray.reshape(dim1: Int, dim2: Int): IntMatrix {
    check(dim1 * dim2 == size) { "IntArray of size $size cannot be reshaped to matrix of dims: [$dim1, $dim2]" }
    return IntMatrix(dim1, dim2) { i, j -> this[i * dim2 + j] }
}
fun IntArray.reshape(dim1: Int, dim2: Int, dim3: Int): IntTensor3 {
    check(dim1 * dim2 * dim3 == size) { "IntArray of size $size cannot be reshaped to matrix of dims: [$dim1, $dim2, $dim3]" }
    return IntTensor3(dim1, dim2, dim3) { i, j, k -> this[((i * dim2) + j) * dim3 + k] }
}
fun IntArray.mapToBool(fn: (Int) -> Boolean = { index -> this[index] != 0 }) = BooleanArray(size, fn)