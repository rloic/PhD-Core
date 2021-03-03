package com.github.rloic.phd.core.cryptography.aes.relatedkey.step1.optimization

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.arrays.IntTensor3
import com.github.rloic.phd.core.cryptography.rijndael.differential.relatedkey.step1.optimization.Solution as RijndaelSolution

class Solution(config: Configuration, objStep1: Int, ΔX: IntTensor3, ΔWK: IntMatrix) :
    RijndaelSolution(config, objStep1, ΔX, ΔWK)