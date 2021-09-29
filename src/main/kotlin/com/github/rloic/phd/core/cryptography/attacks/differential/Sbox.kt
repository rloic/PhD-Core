package com.github.rloic.phd.core.cryptography.attacks.differential

class Sbox(
    private val sbox: IntArray,
) {
    val values = 0 until sbox.size
    val size = sbox.size

    private val invSbox: IntArray = IntArray(sbox.size)

    init {
        for (i in values) {
            invSbox[sbox[i]] = i
        }
    }

    operator fun invoke(n: Int) = sbox[n]
    fun inv(n: Int) = invSbox[n]

}