package com.github.rloic.phd.core.cryptography.ciphers.skinny

import com.github.rloic.phd.core.cryptography.attacks.differential.Sbox
import javaextensions.io.getResourceAsFile
import javaextensions.util.intArrayFrom

class Skinny4 {

    companion object {
        val SBOX = Sbox(
            intArrayFrom(getResourceAsFile("sboxes/skinny64/sb.txt"))
        )
    }

}