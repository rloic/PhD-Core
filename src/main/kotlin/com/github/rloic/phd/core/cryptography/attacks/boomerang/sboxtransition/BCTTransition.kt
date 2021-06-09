@file:Suppress("NonAsciiCharacters")

package com.github.rloic.phd.core.cryptography.attacks.boomerang.sboxtransition

import com.github.rloic.phd.core.cryptography.attacks.boomerang.util.Expr
import com.github.rloic.phd.core.cryptography.attacks.boomerang.SboxTables
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.Variable


class BCTTransition(val γ: Expr, val δ: Expr) : SboxTransition {

    override val variables by lazy { (γ.variables + δ.variables).toSet() }

    override val isInteresting = !γ.isZero && !δ.isZero

    override fun imposeVariable(variable: Variable, value: Int) =
        BCTTransition(
            γ.imposeVariable(variable, value),
            δ.imposeVariable(variable, value)
        )

    override fun getCstProba(tables: SboxTables) = tables.bctProba(γ.ensureCst(), δ.ensureCst())

    override fun getVariableDomain(variable: Variable, tables: SboxTables) =
        outputDiffBCT(γ, δ, tables, variable)
            ?: inputDiffsBCT(δ, γ, tables, variable)
            ?: tables.values

    override fun toString() = "BCT($γ, $δ)"
}