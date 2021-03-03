package com.github.rloic.phd.core.cryptography.aes.singlekey.step1.enumeration

import com.github.rloic.phd.core.cryptography.rijndael.differential.singlekey.step1.enumeration.Configuration as RijndaelConfiguration

class Configuration(Nr: Int, objStep1: Int): RijndaelConfiguration(Nr, 128, objStep1) {
    fun copy(Nr: Int = this.Nr, objStep1: Int = this.objStep1) = Configuration(Nr, objStep1)
    override fun toString() = "Configuration(Nr=$Nr, objStep1=$objStep1)"
}