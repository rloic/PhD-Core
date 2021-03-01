package com.github.rloic.phd.core.cryptography.rijndael.relatedkey.step1.optimization

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.arrays.IntTensor3

open class Solution(
    val config: Configuration,
    val objStep1: Int,
    val ΔX: IntTensor3,
    val ΔWK: IntMatrix
)