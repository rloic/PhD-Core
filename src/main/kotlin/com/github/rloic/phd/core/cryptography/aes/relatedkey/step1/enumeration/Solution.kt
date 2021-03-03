package com.github.rloic.phd.core.cryptography.aes.relatedkey.step1.enumeration

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.arrays.IntTensor3
import com.github.rloic.phd.core.cryptography.rijndael.differential.relatedkey.step1.enumeration.Solution as RijndaelSolution

class Solution(config: Configuration, ΔX: IntTensor3, ΔWK: IntMatrix): RijndaelSolution(config, ΔX, ΔWK)