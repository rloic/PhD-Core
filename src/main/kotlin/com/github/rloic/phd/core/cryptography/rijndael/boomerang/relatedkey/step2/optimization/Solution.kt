package com.github.rloic.phd.core.cryptography.rijndael.boomerang.relatedkey.step2.optimization

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.arrays.IntTensor3
import com.github.rloic.phd.core.arrays.Matrix
import com.github.rloic.phd.core.arrays.Tensor3
import com.github.rloic.phd.core.cryptography.rijndael.Rijndael
import com.github.rloic.phd.core.cryptography.rijndael.boomerang.relatedkey.step1.enumeration.Configuration

class Solution(
    val config: Configuration,
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
    val table: Tensor3<Rijndael.BoomerangTable>,
    val keyProba: Matrix<Int?>,
    val keyTable: Matrix<Rijndael.BoomerangTable>
)