package com.github.phd.core.cryptography.ciphers.rijndael.differential.solutions

import com.github.phd.core.arrays.IntMatrix
import com.github.phd.core.arrays.IntTensor3
import com.github.phd.core.arrays.Matrix
import com.github.phd.core.cryptography.ciphers.rijndael.RkRijndael
import com.github.phd.core.cryptography.ciphers.rijndael.SkRijndael

@Suppress("NonAsciiCharacters")
class OptimizeSkStep2Solution(
    val config: SkRijndael,
    val objStep2: Int,
    val δX: IntTensor3,
    val δSX: IntTensor3,
    val p: IntTensor3,
    val δY: IntTensor3,
    val δZ: IntTensor3,
)

@Suppress("NonAsciiCharacters")
class OptimizeRkStep2Solution(
    val config: RkRijndael,
    val objStep2: Int,
    val δX: IntTensor3,
    val δSX: IntTensor3,
    val p: IntTensor3,
    val δY: IntTensor3,
    val δZ: IntTensor3,
    val δWK: IntMatrix,
    val pWK: Matrix<Int?>
) {

    fun subKey(i: Int) = IntMatrix(4, config.Nb) { j, k -> δWK[j, i * config.Nb + k] }
    fun subKeyProba(i: Int) = Matrix<Int?>(4, config.Nb) { j, k -> pWK[j, i * config.Nb + k] }

}