package com.github.rloic.phd.core.cryptography.aes.relatedkey.step1.optimization

import com.github.rloic.phd.core.cryptography.aes.checkKeySize
import com.github.rloic.phd.core.cryptography.rijndael.differential.relatedkey.step1.optimization.Configuration as RijndaelConfiguration

class Configuration(Nr: Int, keySize: Int): RijndaelConfiguration(Nr, 128, keySize){
    init {
        checkKeySize(keySize)
    }
    fun copy(Nr: Int = this.Nr, keySize: Int = this.keySize) = Configuration(Nr, keySize)
    override fun toString() = "Configuration(Nr=$Nr, keySize=$keySize)"
}