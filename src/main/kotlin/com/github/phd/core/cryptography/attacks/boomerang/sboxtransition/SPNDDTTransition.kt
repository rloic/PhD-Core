@file:Suppress("NonAsciiCharacters")

package com.github.phd.core.cryptography.attacks.boomerang.sboxtransition

import com.github.phd.core.cryptography.attacks.boomerang.util.Expr
import com.github.phd.core.cryptography.attacks.boomerang.SPNSboxTables
import com.github.phd.core.cryptography.ciphers.rijndael.boomerang.Variable

class SPNDDTTransition(val α: Expr, val β: Expr) : SPNSboxTransition {

    override val variables by lazy { (α.variables + β.variables).toSet() }

    override val isInteresting = true

    override fun imposeVariable(variable: Variable, value: Int) =
        SPNDDTTransition(
            α.imposeVariable(variable, value),
            β.imposeVariable(variable, value)
        )

    override fun getCstProba(tables: SPNSboxTables) = tables.ddtProba(α.ensureCst(), β.ensureCst())

    override fun getVariableDomain(variable: Variable, tables: SPNSboxTables) =
        outputDiffDDT(α, β, tables, variable)
            ?: inputDiffsDDT(β, α, tables, variable)
            ?: tables.values


    override fun toString() = "DDT($α, $β)"
}