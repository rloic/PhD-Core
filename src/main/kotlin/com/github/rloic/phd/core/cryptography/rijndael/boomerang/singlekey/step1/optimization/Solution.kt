package com.github.rloic.phd.core.cryptography.rijndael.boomerang.singlekey.step1.optimization

import com.github.rloic.phd.core.arrays.IntTensor3

class Solution(
    val config: Configuration,
    val objStep1: Int,
    val ΔXupper: IntTensor3,
    val freeXupper: IntTensor3,
    val freeSBupper: IntTensor3,
    val ΔXlower: IntTensor3,
    val freeXlower: IntTensor3,
    val freeSBlower: IntTensor3,
    val isTable: IntTensor3,
    val isDDT2: IntTensor3
) {

    val Nb get() = config.Nb

}