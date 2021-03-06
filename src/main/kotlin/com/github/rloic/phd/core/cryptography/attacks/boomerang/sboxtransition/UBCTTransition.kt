@file:Suppress("NonAsciiCharacters")

package com.github.rloic.phd.core.cryptography.attacks.boomerang.sboxtransition

import com.github.rloic.phd.core.cryptography.attacks.boomerang.SboxTables
import com.github.rloic.phd.core.cryptography.attacks.boomerang.util.Expr
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.Variable

class UBCTTransition(val γ: Expr, val θ: Expr, val δ: Expr) : SboxTransition {

    companion object {
        @JvmStatic
        fun transitionOf(gamma: Expr, theta: Expr?, delta: Expr): SboxTransition {
            return when {
                theta == null -> BCTTransition(gamma, delta)
                delta.isZero -> DDTTransition(gamma, theta)
                else -> UBCTTransition(gamma, theta, delta)
            }
        }
    }

    override val variables by lazy {
        (γ.variables + θ.variables + δ.variables).toSet()
    }
    override val isInteresting = true

    override fun imposeVariable(variable: Variable, value: Int) =
        UBCTTransition(
            γ.imposeVariable(variable, value),
            θ.imposeVariable(variable, value),
            δ.imposeVariable(variable, value)
        )

    override fun getCstProba(tables: SboxTables) = tables.ubctProba(γ.ensureCst(), θ.ensureCst(), δ.ensureCst())

    override fun getVariableDomain(variable: Variable, tables: SboxTables) =
        outputDiffDDT(γ, θ, tables, variable)
            ?: inputDiffsDDT(θ, γ, tables, variable)
            ?: tables.values

    override fun toString() = "UBCT($γ, $θ, $δ)"
}