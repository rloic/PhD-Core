package com.github.rloic.phd.core.cryptography.rijndael.boomerang.singlekey.step2.optimization

import com.github.rloic.phd.core.arrays.IntTensor3
import com.github.rloic.phd.core.arrays.Tensor3
import com.github.rloic.phd.core.cryptography.boomerang.BoomerangTable
import com.github.rloic.phd.core.cryptography.rijndael.Rijndael
import com.github.rloic.phd.core.cryptography.rijndael.boomerang.singlekey.step1.enumeration.Configuration

@Suppress("NonAsciiCharacters")
class Solution(
    val config: Configuration,
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