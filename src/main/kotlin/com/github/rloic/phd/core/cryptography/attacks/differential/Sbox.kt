package com.github.rloic.phd.core.cryptography.attacks.differential

class Sbox(
    private val sbox: IntArray,
    private val invSbox: IntArray
) {
    val values = 0 until sbox.size
    val size = sbox.size

    init {
        check(sbox.size == invSbox.size)
        for (i in values) {
            check(invSbox[sbox[i]] == i)
        }
    }

    operator fun invoke(n: Int) = sbox[n]
    fun inv(n: Int) = invSbox[n]

}