package com.github.rloic.phd.core.cryptography.ciphers.rijndael

import com.github.rloic.phd.core.cryptography.attacks.boomerang.SPNSboxTables
import com.github.rloic.phd.core.cryptography.attacks.differential.Sbox
import com.github.rloic.phd.core.cryptography.utils.generateXorTuples
import com.github.rloic.phd.core.cryptography.utils.log2
import javaextensions.io.getResourceAsFile
import javaextensions.util.intArrayFrom
import javaextensions.util.reshape
import org.chocosolver.solver.constraints.extension.Tuples

open class Rijndael(val Nr: Int, val textSize: TextSize) {
    init {
        check(Nr >= 3 && Nr <= MAX_ROUNDS[textSize.nbBits / 32 - 4].maxOrNull()!!)
    }

    @JvmInline
    value class TextSize(val nbBits: Int) {
        init {
            check(nbBits in intArrayOf(128, 160, 192, 224, 256))
        }
    }

    @JvmInline
    value class KeySize(val nbBits: Int) {
        init {
            check(nbBits in intArrayOf(128, 160, 192, 224, 256))
        }
    }

    companion object {
        internal val MAX_ROUNDS = intArrayOf(
            10, 11, 12, 13, 14,
            11, 11, 12, 13, 14,
            12, 12, 12, 13, 14,
            13, 13, 13, 13, 14,
            14, 14, 14, 14, 14,
        ).reshape(5, 5)

        internal val SHIFTS = intArrayOf(
            0, 1, 2, 3,
            0, 1, 2, 3,
            0, 1, 2, 3,
            0, 1, 2, 4,
            0, 1, 3, 4
        ).reshape(5, 4)

        private val SBOX = Sbox(intArrayFrom(getResourceAsFile("sboxes/rijndael/sb.txt")))

        val OPTIMAL_SBOX_TRANSITION = intArrayFrom(getResourceAsFile("sboxes/rijndael/best_sb_transition.txt"))
        val OPTIMAL_INV_SBOX_TRANSITION = intArrayFrom(getResourceAsFile("sboxes/rijndael/best_sb_inv_transition.txt"))

        val XOR_TUPLES = generateXorTuples(SBOX.values)
        val SBOX_TABLES = SPNSboxTables(SBOX)

        val mul2: Tuples = buildTimes(2)
        val mul3: Tuples = buildTimes(3)
        val mul9: Tuples = buildTimes(9)
        val mul11: Tuples = buildTimes(11)
        val mul13: Tuples = buildTimes(13)
        val mul14: Tuples = buildTimes(14)

        private fun buildTimes(n: Int): Tuples {
            val tuples = Tuples(true)
            for (i in SBOX.values) {
                tuples.add(i, galoisFieldMul(n, i))
            }
            return tuples
        }

        @Suppress("NAME_SHADOWING")
        fun galoisFieldMul(a: Int, b: Int): Int {
            var p = 0; var a = a; var b= b
            for (counter in 0 until log2(SBOX.size)) {
                if ((b and 1) != 0) {
                    p = p xor a
                }
                val highBitSet = (a and 0x80 != 0)
                a = a shl 1
                if (highBitSet) a = a xor 0x11B
                b = b shr 1
            }
            return p
        }
    }

    val Nb: Int get() = textSize.nbBits / 32
    val shift: IntArray get() = SHIFTS[Nb - 4]

}