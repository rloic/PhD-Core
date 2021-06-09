@file:Suppress("NonAsciiCharacters")

package com.github.rloic.phd.core.cryptography.attacks.boomerang.sboxtransition

import com.github.rloic.phd.core.cryptography.attacks.boomerang.util.XorExpr
import com.github.rloic.phd.core.cryptography.attacks.boomerang.SboxTables
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.Variable

class EBCTTransition(val γ: XorExpr, val θ: XorExpr, val λ: XorExpr, val δ: XorExpr) : SboxTransition {

    companion object {
        @JvmStatic
        fun transitionOf(γ: XorExpr, θ: XorExpr?, λ: XorExpr?, δ: XorExpr): SboxTransition {
            return when {
                θ == null -> LBCTTransition.transitionOf(γ, λ, δ)
                λ == null -> UBCTTransition.transitionOf(γ, θ, δ)
                γ.isZero && θ.isZero -> DDTTransition(λ, δ)
                λ.isZero && δ.isZero -> DDTTransition(γ, θ)
                γ.isZero && θ.isZero && λ.isZero || δ.isZero -> throw IllegalStateException("")
                else -> EBCTTransition(γ, θ, λ, δ)
            }
        }
    }

    override val variables by lazy { (γ.variables + θ.variables + λ.variables + δ.variables).toSet() }
    override val isInteresting = true

    override fun imposeVariable(variable: Variable, value: Int) =
        EBCTTransition(
            γ.imposeVariable(variable, value),
            θ.imposeVariable(variable, value),
            λ.imposeVariable(variable, value),
            δ.imposeVariable(variable, value)
        )

    override fun getCstProba(tables: SboxTables) =
        tables.ebctProba(γ.ensureCst(), θ.ensureCst(), λ.ensureCst(), δ.ensureCst())

    override fun getVariableDomain(variable: Variable, tables: SboxTables) =
        outputDiffDDT(γ, θ, tables, variable)
            ?: inputDiffsDDT(θ, γ, tables, variable)
            ?: outputDiffDDT(λ, δ, tables, variable)
            ?: inputDiffsDDT(δ, λ, tables, variable)
            ?: tables.values

    override fun toString() = "FBCT($γ, $θ, $λ, $δ)"
}