package com.github.rloic.phd.core.cryptography.rijndael.boomerang.relatedkey.step1.optimization

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.arrays.IntTensor3

@Suppress("NonAsciiCharacters")
class Solution(
    val config: Configuration,
    val objStep1: Int,
    val ΔXupper: IntTensor3,
    val freeXupper: IntTensor3,
    val freeSBupper: IntTensor3,
    val ΔYupper: IntTensor3,
    val freeYupper: IntTensor3,
    val ΔZupper: IntTensor3,
    val freeZupper: IntTensor3,
    val ΔWKupper: IntMatrix,
    val freeWKupper: IntMatrix,
    val freeSWKupper: IntMatrix,
    val ΔXlower: IntTensor3,
    val freeXlower: IntTensor3,
    val freeSBlower: IntTensor3,
    val ΔYlower: IntTensor3,
    val freeYlower: IntTensor3,
    val ΔZlower: IntTensor3,
    val freeZlower: IntTensor3,
    val ΔWKlower: IntMatrix,
    val freeWKlower: IntMatrix,
    val freeSWKlower: IntMatrix,
    val isTable: IntTensor3,
    val isDDT2: IntTensor3,
    val isTableKey: IntMatrix,
    val isDDT2Key: IntMatrix
) {

    val Nb get() = config.Nb
    val Nk get() = config.Nk

}