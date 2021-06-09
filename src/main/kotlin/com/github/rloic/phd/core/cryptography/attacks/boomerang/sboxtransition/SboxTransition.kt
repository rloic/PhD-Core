package com.github.rloic.phd.core.cryptography.attacks.boomerang.sboxtransition

import com.github.rloic.phd.core.cryptography.attacks.boomerang.util.Expr
import com.github.rloic.phd.core.cryptography.attacks.boomerang.SboxTables
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.Variable

interface SboxTransition {

    val variables: Set<Variable>
    val isInteresting: Boolean

    fun imposeVariable(variable: Variable, value: Int): SboxTransition
    fun getCstProba(tables: SboxTables): Double
    fun getVariableDomain(variable: Variable, tables: SboxTables): Set<Int>

    fun inputDiffsDDT(output: Expr, input: Expr, tables: SboxTables, v: Variable): Set<Int>? =
        if (output.isConstant && input.containsOnly(v)) {
            val inputs = tables.getPossibleInputDiffsDDT(output.constant)
            input.xor(inputs)
        } else null

    fun outputDiffDDT(input: Expr, output: Expr, tables: SboxTables, v: Variable): Set<Int>? =
        if (input.isConstant && output.containsOnly(v)) {
            val outputs = tables.getPossibleOutputDiffsDDT(input.constant)
            output.xor(outputs)
        } else null

    fun inputDiffsBCT(output: Expr, input: Expr, tables: SboxTables, v: Variable): Set<Int>? =
        if (output.isConstant && input.containsOnly(v)) {
            val inputs = tables.getPossibleInputDiffsBCT(output.constant)
            input.xor(inputs)
        } else null

    fun outputDiffBCT(input: Expr, output: Expr, tables: SboxTables, v: Variable): Set<Int>? =
        if (input.isConstant && output.containsOnly(v)) {
            val outputs = tables.getPossibleOutputDiffsBCT(input.constant)
            output.xor(outputs)
        } else null

    override fun toString(): String

}