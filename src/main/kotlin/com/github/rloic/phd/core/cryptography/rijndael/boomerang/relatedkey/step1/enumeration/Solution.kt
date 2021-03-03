package com.github.rloic.phd.core.cryptography.rijndael.boomerang.relatedkey.step1.enumeration

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.arrays.IntTensor3

class Solution(
    val config: Configuration,
    val ΔXupper: IntTensor3,
    val freeXupper: IntTensor3,
    val freeSBupper: IntTensor3,
    val ΔWKupper: IntMatrix,
    val freeWKupper: IntMatrix,
    val ΔXlower: IntTensor3,
    val freeXlower: IntTensor3,
    val freeSBlower: IntTensor3,
    val ΔWKlower: IntMatrix,
    val freeWKlower: IntMatrix,
    val isTable: IntTensor3,
    val isDDT2: IntTensor3,
    val isTableKey: IntMatrix,
    val isDDT2Key: IntMatrix
) {

    val Nb get() = config.Nb
    val Nk get() = config.Nk

}