@file:Suppress("NonAsciiCharacters")

package com.github.rloic.phd.core.cryptography.attacks.boomerang.sboxtransition

import com.github.rloic.phd.core.cryptography.attacks.boomerang.util.XorExpr
import com.github.rloic.phd.core.cryptography.attacks.boomerang.SboxTables
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.Variable

class LBCTTransition(val γ: XorExpr, val λ: XorExpr, val δ: XorExpr) : SboxTransition {

    companion object {
        @JvmStatic
        fun transitionOf(gamma: XorExpr, lambda: XorExpr?, δ: XorExpr): SboxTransition {
            return when {
                lambda == null -> BCTTransition(gamma, δ)
                gamma.isZero -> DDTTransition(lambda, δ)
                else -> LBCTTransition(gamma, lambda, δ)
            }
        }
    }

    override val variables by lazy {
        (γ.variables + λ.variables + δ.variables).toSet()
    }

    override val isInteresting = true

    override fun imposeVariable(variable: Variable, value: Int) =
        LBCTTransition(
            γ.imposeVariable(variable, value),
            λ.imposeVariable(variable, value),
            δ.imposeVariable(variable, value)
        )

    override fun getCstProba(tables: SboxTables) = tables.lbctProba(γ.ensureCst(), λ.ensureCst(), δ.ensureCst())

    override fun getVariableDomain(variable: Variable, tables: SboxTables) =
        outputDiffDDT(λ, δ, tables, variable)
            ?: inputDiffsDDT(δ, λ, tables, variable)
            ?: tables.values

    override fun toString() = "LBCT($γ, $λ, $δ)"
}