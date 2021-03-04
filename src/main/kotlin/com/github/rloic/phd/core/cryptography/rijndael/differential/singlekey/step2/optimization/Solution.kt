package com.github.rloic.phd.core.cryptography.rijndael.differential.singlekey.step2.optimization

import com.github.rloic.phd.core.arrays.IntTensor3
import com.github.rloic.phd.core.cryptography.aes.singlekey.step1.enumeration.Configuration

@Suppress("NonAsciiCharacters")
class Solution(
    val config: Configuration,
    val objStep2: Int,
    val δX: IntTensor3,
    val δSX: IntTensor3,
    val δY: IntTensor3,
    val δZ: IntTensor3,
)