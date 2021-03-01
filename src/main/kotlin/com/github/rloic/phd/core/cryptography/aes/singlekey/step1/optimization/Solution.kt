package com.github.rloic.phd.core.cryptography.aes.singlekey.step1.optimization

import com.github.rloic.phd.core.arrays.IntTensor3
import com.github.rloic.phd.core.cryptography.rijndael.singlekey.step1.optimization.Solution as RijndaelSolution

class Solution(config: Configuration, objStep1: Int, ΔX: IntTensor3): RijndaelSolution(config, objStep1, ΔX)