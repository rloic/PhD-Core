package com.github.phd.core.cryptography.ciphers.rijndael.differential.solutions

import com.github.phd.core.arrays.IntMatrix
import com.github.phd.core.arrays.IntTensor3
import com.github.phd.core.cryptography.ciphers.rijndael.RkRijndael
import com.github.phd.core.cryptography.ciphers.rijndael.SkRijndael

@Suppress("NonAsciiCharacters")
open class OptimizeSkStep1Solution(
    val config: SkRijndael,
    val objStep1: Int,
    val ΔX: IntTensor3,
)

@Suppress("NonAsciiCharacters")
open class EnumerateSkStep1Solution(
    val config: RkRijndael,
    val ΔX: IntTensor3,
)

@Suppress("NonAsciiCharacters")
open class OptimizeRkStep1Solution(
    val config: RkRijndael,
    val objStep1: Int,
    val ΔX: IntTensor3,
    val ΔWK: IntMatrix,
)

@Suppress("NonAsciiCharacters")
open class EnumerateRkStep1Solution(
    val config: RkRijndael,
    val ΔX: IntTensor3,
    val ΔWK: IntMatrix,
)