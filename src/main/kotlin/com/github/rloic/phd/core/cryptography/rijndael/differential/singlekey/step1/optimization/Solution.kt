package com.github.rloic.phd.core.cryptography.rijndael.differential.singlekey.step1.optimization

import com.github.rloic.phd.core.arrays.IntTensor3

@Suppress("NonAsciiCharacters")
open class Solution(
    val config: Configuration,
    val objStep1: Int,
    val ΔX: IntTensor3
)