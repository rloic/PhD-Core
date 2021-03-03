package com.github.rloic.phd.core.cryptography.aes.singlekey.step1.enumeration

import com.github.rloic.phd.core.arrays.IntTensor3
import com.github.rloic.phd.core.cryptography.rijndael.differential.singlekey.step1.enumeration.Solution as RijndaelSolution

class Solution(config: Configuration, ΔX: IntTensor3) : RijndaelSolution(config, ΔX)