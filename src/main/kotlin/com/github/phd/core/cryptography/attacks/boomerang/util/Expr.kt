package com.github.phd.core.cryptography.attacks.boomerang.util;

import com.github.phd.core.cryptography.ciphers.rijndael.boomerang.Variable


interface Expr {

    val constant: Int
    val variables: List<Variable>

    val isConstant: Boolean
    val isZero: Boolean

    fun xor(constant: Int): Expr

    fun xor(variable: Variable): Expr

    fun xor(dom: Set<Int>): Set<Int>

    fun ensureCst(): Int

    fun evaluate(affectation: Map<Variable, Int>): Expr

    fun imposeVariable(variable: Variable, value: Int): Expr

    fun containsOnly(variable: Variable): Boolean

}
