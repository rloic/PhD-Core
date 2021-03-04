package com.github.rloic.phd.core.cryptography.rijndael.differential.relatedkey.step1.optimization

import com.github.rloic.phd.core.cryptography.rijndael.checkBlockSize
import com.github.rloic.phd.core.cryptography.rijndael.checkKeySize
import com.github.rloic.phd.core.cryptography.rijndael.checkNr

open class Configuration(val Nr: Int, val textSize: Int, val keySize: Int) {

    val Nb: Int get() = textSize / 32
    val Nk: Int get() = keySize / 32

    fun isSbColumn(ik: Int) =  ik >= Nk - 1 && (ik != Nb * (Nr + 1) - 1) && ((ik % Nk == Nk - 1) || (Nk > 6 && ik % Nk == 3))

    init {
        checkNr(Nr)
        checkBlockSize(textSize)
        checkKeySize(keySize)
    }

    fun copy(Nr: Int = this.Nr, blockSize: Int = this.textSize, keySize: Int = this.keySize) =
        Configuration(Nr, blockSize, keySize)

    override fun toString() = "Configuration(Nr=$Nr, textSize=$textSize, keySize=$keySize)"
}