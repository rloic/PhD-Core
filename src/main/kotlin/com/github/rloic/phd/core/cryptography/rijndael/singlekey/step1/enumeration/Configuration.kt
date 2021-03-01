package com.github.rloic.phd.core.cryptography.rijndael.singlekey.step1.enumeration

import com.github.rloic.phd.core.cryptography.rijndael.checkBlockSize
import com.github.rloic.phd.core.cryptography.rijndael.checkNr
import com.github.rloic.phd.core.cryptography.rijndael.checkObjStep1

open class Configuration(val Nr: Int, val blockSize: Int, val objStep1: Int) {

    val Nb: Int get() = blockSize / 32

    init {
        checkNr(Nr)
        checkBlockSize(blockSize)
        checkObjStep1(objStep1)
    }
    fun copy(Nr: Int = this.Nr, blockSize: Int = this.blockSize, objStep1: Int = this.objStep1) = Configuration(Nr, blockSize, objStep1)
    override fun toString() = "Configuration(Nr=$Nr, blockSize=$blockSize, objStep1=$objStep1)"
}