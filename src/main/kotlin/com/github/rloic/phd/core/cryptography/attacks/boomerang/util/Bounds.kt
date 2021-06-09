package com.github.rloic.phd.core.cryptography.attacks.boomerang.util

data class Bounds(val min: Int, val max: Int) {
    init {
        check(max >= min)
    }

    operator fun plus(other: Bounds) = Bounds(min + other.min, max + other.max)
}