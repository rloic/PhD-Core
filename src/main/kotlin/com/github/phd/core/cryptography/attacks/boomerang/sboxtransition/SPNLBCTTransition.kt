@file:Suppress("NonAsciiCharacters")

package com.github.phd.core.cryptography.attacks.boomerang.sboxtransition

import com.github.phd.core.cryptography.attacks.boomerang.util.XorExpr
import com.github.phd.core.cryptography.attacks.boomerang.SPNSboxTables
import com.github.phd.core.cryptography.ciphers.rijndael.boomerang.Variable

class SPNLBCTTransition(val γ: XorExpr, val λ: XorExpr, val δ: XorExpr) : SPNSboxTransition {

    companion object {
        @JvmStatic
        fun transitionOf(gamma: XorExpr, lambda: XorExpr?, δ: XorExpr): SPNSboxTransition {
            return when {
                lambda == null -> SPNBCTTransition(gamma, δ)
                gamma.isZero -> SPNDDTTransition(lambda, δ)
                else -> SPNLBCTTransition(gamma, lambda, δ)
            }
        }
    }

    override val variables by lazy {
        (γ.variables + λ.variables + δ.variables).toSet()
    }

    override val isInteresting = true

    override fun imposeVariable(variable: Variable, value: Int) =
        SPNLBCTTransition(
            γ.imposeVariable(variable, value),
            λ.imposeVariable(variable, value),
            δ.imposeVariable(variable, value)
        )

    override fun getCstProba(tables: SPNSboxTables) = tables.lbctProba(γ.ensureCst(), λ.ensureCst(), δ.ensureCst())

    override fun getVariableDomain(variable: Variable, tables: SPNSboxTables) =
        outputDiffDDT(λ, δ, tables, variable)
            ?: inputDiffsDDT(δ, λ, tables, variable)
            ?: tables.values

    override fun toString() = "LBCT($γ, $λ, $δ)"
}