package com.github.rloic.phd.core.cryptography.aes

fun checkKeySize(keySize: Int) {
    if(keySize !in intArrayOf(128, 192, 256)) {
        throw IllegalArgumentException("Accepted key size = { 128, 192, 256 }, given: $keySize")
    }
}