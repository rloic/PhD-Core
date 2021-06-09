package com.github.rloic.phd.core.cryptography.utils

fun log2(n: Int): Int {
    var log2 = 1
    var count = 0
    while (log2 < n) {
        count += 1
        log2 = log2 shl 1
    }
    check(log2 == n) { "$n is not a power of 2" }
    return count - 1
}