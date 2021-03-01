package com.github.rloic.phd.core.cryptography.rijndael.singlekey.step1.optimization

import com.github.rloic.phd.core.arrays.IntTensor3

open class Solution(
    val config: Configuration,
    val objStep1: Int,
    val ΔX: IntTensor3
)