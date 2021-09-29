@file:Suppress("NonAsciiCharacters")

package com.github.rloic.phd.core.cryptography.attacks.boomerang.sboxtransition

import com.github.rloic.phd.core.cryptography.attacks.boomerang.SPNSboxTables
import com.github.rloic.phd.core.cryptography.attacks.boomerang.util.Expr
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.Variable

class SPNUBCTTransition(val γ: Expr, val θ: Expr, val δ: Expr) : SPNSboxTransition {

    companion object {
        @JvmStatic
        fun transitionOf(gamma: Expr, theta: Expr?, delta: Expr): SPNSboxTransition {
            return when {
                theta == null -> SPNBCTTransition(gamma, delta)
                delta.isZero -> SPNDDTTransition(gamma, theta)
                else -> SPNUBCTTransition(gamma, theta, delta)
            }
        }
    }

    override val variables by lazy {
        (γ.variables + θ.variables + δ.variables).toSet()
    }
    override val isInteresting = true

    override fun imposeVariable(variable: Variable, value: Int) =
        SPNUBCTTransition(
            γ.imposeVariable(variable, value),
            θ.imposeVariable(variable, value),
            δ.imposeVariable(variable, value)
        )

    override fun getCstProba(tables: SPNSboxTables) = tables.ubctProba(γ.ensureCst(), θ.ensureCst(), δ.ensureCst())

    override fun getVariableDomain(variable: Variable, tables: SPNSboxTables) =
        outputDiffDDT(γ, θ, tables, variable)
            ?: inputDiffsDDT(θ, γ, tables, variable)
            ?: tables.values

    override fun toString() = "UBCT($γ, $θ, $δ)"
}