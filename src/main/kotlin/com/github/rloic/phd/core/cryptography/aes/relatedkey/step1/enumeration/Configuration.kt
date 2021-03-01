package com.github.rloic.phd.core.cryptography.aes.relatedkey.step1.enumeration

import com.github.rloic.phd.core.cryptography.rijndael.checkNr
import com.github.rloic.phd.core.cryptography.rijndael.relatedkey.step1.enumeration.Configuration as RijndaelConfiguration

class Configuration(Nr: Int, keySize: Int, objStep1: Int): RijndaelConfiguration(Nr, 128, keySize, objStep1) {
    init {
        checkNr(Nr)
    }

    fun copy(Nr: Int = this.Nr, keySize: Int = this.keySize, objStep1: Int = this.objStep1) = Configuration(Nr, keySize, objStep1)
    override fun toString() = "Configuration(Nr=$Nr, keySize=$keySize, objStep1=$objStep1)"
}