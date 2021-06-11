package com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.solutions

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.arrays.IntTensor3
import com.github.rloic.phd.core.arrays.Matrix
import com.github.rloic.phd.core.arrays.Tensor3
import com.github.rloic.phd.core.cryptography.attacks.ConfiguredBy
import com.github.rloic.phd.core.cryptography.attacks.boomerang.BoomerangTable
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.RkRijndael
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.SkRijndael

interface Step2RijndaelBoomerangCipher {
    val δXupper: IntTensor3
    val δSXupper: IntTensor3
    val δYupper: IntTensor3
    val δZupper: IntTensor3
    val δXlower: IntTensor3
    val δSXlower: IntTensor3
    val δYlower: IntTensor3
    val δZlower: IntTensor3
    val proba: Tensor3<Int?>
    val table: Tensor3<BoomerangTable>
}

interface Step2RijndaelBoomerangCipherWithKeySchedule : Step2RijndaelBoomerangCipher {
    val δWKupper: IntMatrix
    val δSWKupper: Matrix<Int?>
    val δWKlower: IntMatrix
    val δSWKlower: Matrix<Int?>
    val keyProba: Matrix<Int?>
    val keyTable: Matrix<BoomerangTable>
}

fun <T> T.subKeyUpper(i: Int) where T: ConfiguredBy<RkRijndael>, T: Step2RijndaelBoomerangCipherWithKeySchedule =
    IntMatrix(4, config.Nb) { j, k -> δWKupper[j, i * config.Nb + k] }

fun <T> T.subSKeyUpper(i: Int) where T: ConfiguredBy<RkRijndael>, T: Step2RijndaelBoomerangCipherWithKeySchedule =
    Matrix(4, config.Nb) { j, k -> δSWKupper[j, i * config.Nb + k] }

fun <T> T.subKeyLower(i: Int) where T: ConfiguredBy<RkRijndael>, T: Step2RijndaelBoomerangCipherWithKeySchedule =
    IntMatrix(4, config.Nb) { j, k -> δWKlower[j, i * config.Nb + k] }

fun <T> T.subSKeyLower(i: Int) where T: ConfiguredBy<RkRijndael>, T: Step2RijndaelBoomerangCipherWithKeySchedule =
    Matrix(4, config.Nb) { j, k -> δSWKlower[j, i * config.Nb + k] }

fun <T> T.subKeyTable(i: Int) where T: ConfiguredBy<RkRijndael>, T: Step2RijndaelBoomerangCipherWithKeySchedule =
    Matrix(4, config.Nb) { j, k -> keyTable[j, i * config.Nb + k] }

@Suppress("NonAsciiCharacters")
class OptimizeSkStep2Solution(
    override val config: SkRijndael,
    val objStep2: Int,
    override val δXupper: IntTensor3,
    override val δSXupper: IntTensor3,
    override val δYupper: IntTensor3,
    override val δZupper: IntTensor3,
    override val δXlower: IntTensor3,
    override val δSXlower: IntTensor3,
    override val δYlower: IntTensor3,
    override val δZlower: IntTensor3,
    override val proba: Tensor3<Int?>,
    override val table: Tensor3<BoomerangTable>,
) : ConfiguredBy<SkRijndael>, Step2RijndaelBoomerangCipher

@Suppress("NonAsciiCharacters")
class OptimizeRkStep2Solution(
    override val config: RkRijndael,
    val objStep2: Int,
    override val δXupper: IntTensor3,
    override val δSXupper: IntTensor3,
    override val δYupper: IntTensor3,
    override val δZupper: IntTensor3,
    override val δWKupper: IntMatrix,
    override val δSWKupper: Matrix<Int?>,
    override val δXlower: IntTensor3,
    override val δSXlower: IntTensor3,
    override val δYlower: IntTensor3,
    override val δZlower: IntTensor3,
    override val δWKlower: IntMatrix,
    override val δSWKlower: Matrix<Int?>,
    override val proba: Tensor3<Int?>,
    override val table: Tensor3<BoomerangTable>,
    override val keyProba: Matrix<Int?>,
    override val keyTable: Matrix<BoomerangTable>
) : ConfiguredBy<RkRijndael>, Step2RijndaelBoomerangCipherWithKeySchedule