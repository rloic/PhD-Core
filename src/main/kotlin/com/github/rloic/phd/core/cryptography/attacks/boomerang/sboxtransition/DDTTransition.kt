@file:Suppress("NonAsciiCharacters")

package com.github.rloic.phd.core.cryptography.attacks.boomerang.sboxtransition

import com.github.rloic.phd.core.cryptography.attacks.boomerang.util.Expr
import com.github.rloic.phd.core.cryptography.attacks.boomerang.SboxTables
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.Variable

class DDTTransition(val α: Expr, val β: Expr) : SboxTransition {

    override val variables by lazy { (α.variables + β.variables).toSet() }

    override val isInteresting = true

    override fun imposeVariable(variable: Variable, value: Int) =
        DDTTransition(
            α.imposeVariable(variable, value),
            β.imposeVariable(variable, value)
        )

    override fun getCstProba(tables: SboxTables) = tables.ddtProba(α.ensureCst(), β.ensureCst())

    override fun getVariableDomain(variable: Variable, tables: SboxTables) =
        outputDiffDDT(α, β, tables, variable)
            ?: inputDiffsDDT(β, α, tables, variable)
            ?: tables.values


    override fun toString() = "DDT($α, $β)"
}