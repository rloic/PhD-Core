package com.github.rloic.phd.core.cryptography.rijndael.relatedkey.step1.enumeration

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.arrays.IntTensor3

open class Solution(
    val config: Configuration,
    val ΔX: IntTensor3,
    val ΔWK: IntMatrix
)