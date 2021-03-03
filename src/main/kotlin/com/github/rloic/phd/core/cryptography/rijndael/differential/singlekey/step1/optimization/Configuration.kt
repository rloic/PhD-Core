package com.github.rloic.phd.core.cryptography.rijndael.differential.singlekey.step1.optimization

import com.github.rloic.phd.core.cryptography.rijndael.checkBlockSize
import com.github.rloic.phd.core.cryptography.rijndael.checkNr
import com.github.rloic.phd.core.cryptography.rijndael.differential.relatedkey.step1.optimization.Configuration as RkConfiguration

open class Configuration(val Nr: Int, val blockSize: Int) {

    val Nb: Int get() = blockSize / 32

    init {
        checkNr(Nr)
        checkBlockSize(blockSize)
    }
    fun copy(Nr: Int = this.Nr, blockSize: Int = this.blockSize) = Configuration(Nr, blockSize)
    override fun toString() = "Configuration(Nr=$Nr, blockSize=$blockSize)"

    fun withKeySize(keySize: Int) = RkConfiguration(Nr, blockSize, keySize)

}