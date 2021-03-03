package com.github.rloic.phd.core.cryptography.aes.singlekey.step1.optimization

import com.github.rloic.phd.core.cryptography.rijndael.differential.singlekey.step1.optimization.Configuration as RijndaelConfiguration

class Configuration(Nr: Int) : RijndaelConfiguration(Nr, 128) {
    fun copy(Nr: Int = this.Nr) = Configuration(Nr)
    override fun toString() = "Configuration(Nr=$Nr)"
}