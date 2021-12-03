package com.github.phd.core.cryptography.attacks.boomerang.util

import com.github.phd.core.cryptography.ciphers.rijndael.boomerang.Variable

class XorExpr private constructor(
    override val constant: Int,
    override val variables: List<Variable>
) : Expr {

    companion object {
        @JvmStatic
        fun ofConstant(constant: Int) = XorExpr(constant, emptyList())

        @JvmStatic
        fun ofVariable(variable: Variable) = XorExpr(0, listOf(variable))
    }

    override fun xor(constant: Int) = XorExpr(this.constant xor constant, variables)
    override fun xor(variable: Variable) = XorExpr(constant, variables + variable)

    override val isConstant get() = variables.isEmpty()

    override val isZero get() = isConstant && constant == 0

    override fun ensureCst() =
        if (isConstant) constant
        else throw IllegalStateException("$this is not a constant")

    override fun evaluate(affectation: Map<Variable, Int>): XorExpr {
        val (cstVars, otherVars) = variables.split(affectation::containsKey)
        val constant = cstVars.reduceMap(constant) { acc, v -> acc xor affectation.getValue(v) }

        return XorExpr(constant, otherVars)
    }

    override fun imposeVariable(variable: Variable, value: Int): XorExpr {
        val (cstVars, otherVars) = variables.split { it == variable }
        val constant = cstVars.reduceMap(constant) { acc, _ -> acc xor value }
        return XorExpr(constant, otherVars)
    }

    override fun containsOnly(variable: Variable) = variable in variables && variables.size == 1

    override fun xor(dom: Set<Int>): Set<Int> {
        return if (constant == 0) dom
        else dom.map { it xor constant }.toSet()
    }

    override fun toString() = buildString {
        append(constant)

        if (constant != 0 && variables.isNotEmpty()) {
            append(" + ")
        }
        append(variables.joinToString(" + "))
    }
}

private fun <T> Iterable<T>.split(isLeft: (T) -> Boolean): Pair<List<T>, List<T>> {
    val left = mutableListOf<T>()
    val right = mutableListOf<T>()
    for (element in this) {
        if (isLeft(element)) left += element
        else right += element
    }
    return Pair(left, right)
}

private fun <T, U> Iterable<T>.reduceMap(init: U, acc: (acc: U, v: T) -> U): U {
    var value = init
    for (element in this) {
        value = acc(value, element)
    }
    return value
}