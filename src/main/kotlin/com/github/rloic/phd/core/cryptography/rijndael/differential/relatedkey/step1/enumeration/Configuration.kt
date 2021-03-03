package com.github.rloic.phd.core.cryptography.rijndael.differential.relatedkey.step1.enumeration

import com.github.rloic.phd.core.cryptography.rijndael.checkBlockSize
import com.github.rloic.phd.core.cryptography.rijndael.checkKeySize
import com.github.rloic.phd.core.cryptography.rijndael.checkNr
import com.github.rloic.phd.core.cryptography.rijndael.checkObjStep1

open class Configuration(val Nr: Int, val blockSize: Int, val keySize: Int, val objStep1: Int) {

    val Nb: Int get() = blockSize / 32
    val Nk: Int get() = keySize / 32

    fun isSbColumn(ik: Int) =  ik >= Nk - 1 && ((ik % Nk == Nk - 1) || (Nk > 6 && ik % Nk == 3))

    init {
        checkNr(Nr)
        checkBlockSize(blockSize)
        checkKeySize(keySize)
        checkObjStep1(objStep1)
    }

    fun copy(
        Nr: Int = this.Nr,
        blockSize: Int = this.blockSize,
        keySize: Int = this.keySize,
        objStep1: Int = this.objStep1
    ) = Configuration(Nr, blockSize, keySize, objStep1)

    override fun toString() = "Configuration(Nr=$Nr, blockSize=$blockSize, keySize=$keySize, objStep1=$objStep1)"
}