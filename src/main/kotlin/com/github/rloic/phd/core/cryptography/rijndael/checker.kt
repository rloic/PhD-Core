package com.github.rloic.phd.core.cryptography.rijndael

fun checkNr(Nr: Int) {
    if (Nr < 3) {
        throw IllegalArgumentException("Number of rounds must be greater or equals than 3, given: $Nr")
    }
}

fun checkBlockSize(blockSize: Int) {
    if(blockSize !in intArrayOf(128, 160, 192, 224, 256)) {
        throw IllegalArgumentException("Accepted block size = { 128, 160, 192, 224, 256 }, given: $blockSize")
    }
}

fun checkKeySize(keySize: Int) {
    if(keySize !in intArrayOf(128, 160, 192, 224, 256)) {
        throw IllegalArgumentException("Accepted key size = { 128, 160, 192, 224, 256 }, given: $keySize")
    }
}

fun checkObjStep1(objStep1: Int) {
    if (objStep1 < 0) {
        throw IllegalArgumentException("ObjStep1 must be greater or equals than 0, given: $$objStep1")
    }
}