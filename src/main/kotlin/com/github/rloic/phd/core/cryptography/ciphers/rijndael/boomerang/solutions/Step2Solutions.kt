package com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.solutions

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.arrays.IntTensor3
import com.github.rloic.phd.core.arrays.Matrix
import com.github.rloic.phd.core.arrays.Tensor3
import com.github.rloic.phd.core.cryptography.attacks.boomerang.BoomerangTable
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.RkRijndael
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.SkRijndael

@Suppress("NonAsciiCharacters")
class OptimizeSkStep2Solution(
    val config: SkRijndael,
    val objStep2: Int,
    val δXupper: IntTensor3,
    val δSXupper: IntTensor3,
    val δYupper: IntTensor3,
    val δZupper: IntTensor3,
    val δXlower: IntTensor3,
    val δSXlower: IntTensor3,
    val δYlower: IntTensor3,
    val δZlower: IntTensor3,
    val proba: Tensor3<Int?>,
    val table: Tensor3<BoomerangTable>,
)

@Suppress("NonAsciiCharacters")
class OptimizeRkStep2Solution(
    val config: RkRijndael,
    val objStep2: Int,
    val δXupper: IntTensor3,
    val δSXupper: IntTensor3,
    val δYupper: IntTensor3,
    val δZupper: IntTensor3,
    val δWKupper: IntMatrix,
    val δSWKupper: Matrix<Int?>,
    val δXlower: IntTensor3,
    val δSXlower: IntTensor3,
    val δYlower: IntTensor3,
    val δZlower: IntTensor3,
    val δWKlower: IntMatrix,
    val δSWKlower: Matrix<Int?>,
    val proba: Tensor3<Int?>,
    val table: Tensor3<BoomerangTable>,
    val keyProba: Matrix<Int?>,
    val keyTable: Matrix<BoomerangTable>
) {

    fun subKeyUpper(i: Int) = IntMatrix(4, config.Nb) { j, k -> δWKupper[j, i * config.Nb + k] }
    fun subSKeyUpper(i: Int) = Matrix(4, config.Nb) { j, k -> δSWKupper[j, i * config.Nb + k] }
    fun subKeyLower(i: Int) = IntMatrix(4, config.Nb) { j, k -> δWKlower[j, i * config.Nb + k] }
    fun subSKeyLower(i: Int) = Matrix(4, config.Nb) { j, k -> δSWKlower[j, i * config.Nb + k] }
    fun subKeyTable(i: Int) = Matrix(4, config.Nb) { j, k -> keyTable[j, i * config.Nb + k] }

}