package com.github.rloic.phd.core.cryptography.utils

import org.chocosolver.solver.constraints.extension.Tuples

fun generateXorTuples(range: IntRange): Tuples {
    val tuples = Tuples()
    for (i in range) {
        for (j in range) {
            tuples.add(i, j, i xor j)
        }
    }
    return tuples
}