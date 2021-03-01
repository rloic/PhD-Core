package com.github.rloic.phd.core.cryptography.rijndael.singlekey.step1.optimization

import com.github.rloic.phd.core.cryptography.rijndael.checkBlockSize
import com.github.rloic.phd.core.cryptography.rijndael.checkNr

open class Configuration(val Nr: Int, val blockSize: Int) {

    val Nb: Int get() = blockSize / 32

    init {
        checkNr(Nr)
        checkBlockSize(blockSize)
    }
    fun copy(Nr: Int = this.Nr, blockSize: Int = this.blockSize) = Configuration(Nr, blockSize)
    override fun toString() = "Configuration(Nr=$Nr, blockSize=$blockSize)"
}