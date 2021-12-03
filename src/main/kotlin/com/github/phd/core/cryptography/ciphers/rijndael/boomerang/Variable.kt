package com.github.phd.core.cryptography.ciphers.rijndael.boomerang

/**
 * A class representing a variable. It contains an id, and the position in the com.github.phd.boomerang.sandwichproba.trails
 *
 * @author Mathieu Vavrille
 */
class Variable(val posRound: Int, val posI: Int, val posJ: Int) : Comparable<Variable> {

    companion object {
        private var created_counter = 0
    }

    private val varNb: Int = created_counter

    init {
        created_counter++
    }

    override fun toString(): String {
        return "V$varNb-$posRound-$posI-$posJ"
    }

    override operator fun compareTo(other: Variable): Int {
        return varNb.compareTo(other.varNb)
    }

}
