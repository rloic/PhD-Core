package com.github.rloic.phd.core.cryptography.rijndael.differential.relatedkey.step2.optimization

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.arrays.IntTensor3
import com.github.rloic.phd.core.arrays.Matrix
import com.github.rloic.phd.core.cryptography.rijndael.differential.relatedkey.step1.enumeration.Configuration

@Suppress("NonAsciiCharacters")
class Solution(
    val config: Configuration,
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