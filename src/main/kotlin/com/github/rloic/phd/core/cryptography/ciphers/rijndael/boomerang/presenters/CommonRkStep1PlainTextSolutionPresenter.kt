package com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.presenters

import com.github.rloic.phd.core.arrays.Matrix
import com.github.rloic.phd.core.arrays.Tensor3
import com.github.rloic.phd.core.cryptography.attacks.boomerang.BoomerangRules
import com.github.rloic.phd.core.cryptography.attacks.boomerang.BoomerangTable
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.solutions.BoomerangSbVar
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.solutions.BoomerangVar
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.solutions.OptionalBoomerangSbVar
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.solutions.TrailVar
import com.github.rloic.phd.core.io.Console

@Suppress("NonAsciiCharacters")
class CommonRkStep1PlainTextSolutionPresenter(val out: Appendable) {

    val LIGHT_GRAY = 0xCC_CC_CC
    val DARK_GRAY = 0x77_77_77

    private fun color(t: BoomerangTable?) = when (t) {
        null, BoomerangTable.None -> 0xffffff
        BoomerangTable.DDT -> 0xcc554f
        BoomerangTable.BCT -> 0x4aad92
        BoomerangTable.DDT2 -> 0xc45ca2
        BoomerangTable.UBCT -> 0x7aa444
        BoomerangTable.LBCT -> 0x7878cd
        BoomerangTable.EBCT -> 0xc1883f
    }

    val VALUE_LENGTH = 3

    private fun table(
        X: Tensor3<BoomerangSbVar>
    ): Tensor3<BoomerangTable> {
        return Tensor3(X.dim1, X.dim2, X.dim3) { i, j, k ->
            when {
                BoomerangRules.isDDT(X[i, j, k].Δ.upper, X[i, j, k].free.upper, X[i, j, k].freeS.upper, X[i, j, k].Δ.lower, X[i, j, k].free.lower, X[i, j, k].freeS.lower) -> {
                    BoomerangTable.DDT
                }
                BoomerangRules.isDDT2(X[i, j, k].Δ.upper, X[i, j, k].free.upper, X[i, j, k].freeS.upper, X[i, j, k].Δ.lower, X[i, j, k].free.lower, X[i, j, k].freeS.lower) -> {
                    BoomerangTable.DDT2
                }
                BoomerangRules.isBCT(X[i, j, k].Δ.upper, X[i, j, k].free.upper, X[i, j, k].freeS.upper, X[i, j, k].Δ.lower, X[i, j, k].free.lower, X[i, j, k].freeS.lower) -> {
                    BoomerangTable.BCT
                }
                BoomerangRules.isUBCT(X[i, j, k].Δ.upper, X[i, j, k].free.upper, X[i, j, k].freeS.upper, X[i, j, k].Δ.lower, X[i, j, k].free.lower, X[i, j, k].freeS.lower) -> {
                    BoomerangTable.UBCT
                }
                BoomerangRules.isLBCT(X[i, j, k].Δ.upper, X[i, j, k].free.upper, X[i, j, k].freeS.upper, X[i, j, k].Δ.lower, X[i, j, k].free.lower, X[i, j, k].freeS.lower) -> {
                    BoomerangTable.LBCT
                }
                BoomerangRules.isEBCT(X[i, j, k].Δ.upper, X[i, j, k].free.upper, X[i, j, k].freeS.upper, X[i, j, k].Δ.lower, X[i, j, k].free.lower, X[i, j, k].freeS.lower) -> {
                    BoomerangTable.EBCT
                }
                else -> {
                    BoomerangTable.None
                }
            }
        }
    }

    private fun keyTable(
        WK: Matrix<OptionalBoomerangSbVar>
    ): Matrix<BoomerangTable> {
        return Matrix(WK.dim1, WK.dim2) { j, k ->
            when {
                BoomerangRules.isDDT(WK[j, k].Δ.upper, WK[j, k].free.upper, WK[j, k].freeS?.upper, WK[j, k].Δ.lower, WK[j, k].free.lower, WK[j, k].freeS?.lower) -> {
                    BoomerangTable.DDT
                }
                BoomerangRules.isDDT2(WK[j, k].Δ.upper, WK[j, k].free.upper, WK[j, k].freeS?.upper, WK[j, k].Δ.lower, WK[j, k].free.lower, WK[j, k].freeS?.lower) -> {
                    BoomerangTable.DDT2
                }
                BoomerangRules.isBCT(WK[j, k].Δ.upper, WK[j, k].free.upper, WK[j, k].freeS?.upper, WK[j, k].Δ.lower, WK[j, k].free.lower, WK[j, k].freeS?.lower) -> {
                    BoomerangTable.BCT
                }
                BoomerangRules.isUBCT(WK[j, k].Δ.upper, WK[j, k].free.upper, WK[j, k].freeS?.upper, WK[j, k].Δ.lower, WK[j, k].free.lower, WK[j, k].freeS?.lower) -> {
                    BoomerangTable.UBCT
                }
                BoomerangRules.isLBCT(WK[j, k].Δ.upper, WK[j, k].free.upper, WK[j, k].freeS?.upper, WK[j, k].Δ.lower, WK[j, k].free.lower, WK[j, k].freeS?.lower) -> {
                    BoomerangTable.LBCT
                }
                BoomerangRules.isEBCT(WK[j, k].Δ.upper, WK[j, k].free.upper, WK[j, k].freeS?.upper, WK[j, k].Δ.lower, WK[j, k].free.lower, WK[j, k].freeS?.lower) -> {
                    BoomerangTable.EBCT
                }
                else -> {
                    BoomerangTable.None
                }
            }
        }
    }

    private fun content(xValue: Int, freeValue: Int, isKSSbOut: Boolean = false): String {
        return if (freeValue == 1) {
            Console.color(DARK_GRAY) + (if (isKSSbOut) "(F)" else " F ")
        } else {
            if (xValue == 1) {
                (if (isKSSbOut) "(X)" else " X ")
            } else {
                Console.color(LIGHT_GRAY) + (if (isKSSbOut) "(0)" else " 0 ")
            }
        }
    }

    private fun keySize(r: Int, Nb: Int, Nk: Int, isSbColumn: (Int) -> Boolean): Int {
        var res = Nb
        for (k in 0 until Nb) {
            if (isSbColumn(r * Nb + k)) {
                res += 1
            }
        }
        return res
    }

    private fun presentKey(
        Nr: Int, Nb: Int, Nk: Int, trail: (TrailVar) -> Int,
        WK: Matrix<OptionalBoomerangSbVar>,
        transitions: Matrix<BoomerangTable>,
        isSbColumn: (Int) -> Boolean
    ) {
        for (j in 0..3) {
            for (ik in 0 until Nb * (Nr + 1)) {
                if (isSbColumn(ik)) {
                    out.append(Console.bg(color(transitions[j, ik])) + content(trail(WK[j, ik].Δ), trail(WK[j, ik].free)) + Console.reset())
                    out.append(Console.bg(color(transitions[j, ik])) + content(trail(WK[j, ik].Δ), trail(WK[j, ik].freeS!!), true) + Console.reset())
                } else {
                    out.append(content(trail(WK[j, ik].Δ), trail(WK[j, ik].free)) + Console.reset())
                }

                if (ik % Nb == Nb - 1) {
                    val nbBlocks = if (ik / Nb < Nr - 1) 4 else 3
                    repeat((Nb + 1) * nbBlocks - 1) { out.append("   ") }
                }
            }
            out.append("\n")
        }
    }

    private fun presentText(
        Nr: Int, Nb: Int, Nk: Int, selector: (TrailVar) -> Int,
        X: Tensor3<BoomerangSbVar>,
        table: Tensor3<BoomerangTable>,
        Y: Tensor3<BoomerangVar>,
        Z: Tensor3<BoomerangVar>,
        isSbColumn: (Int) -> Boolean
    ) {
        repeat(keySize(0, Nb, Nk, isSbColumn) * VALUE_LENGTH) { out.append(' ') }
        for (i in 0 until Nr) {
            out.append("ΔX[$i]")
            repeat((Nb + 1) * VALUE_LENGTH - "ΔX[$i]".length) { out.append(' ') }
            out.append("ΔSX[$i]")
            repeat((Nb + 1) * VALUE_LENGTH - "ΔSX[$i]".length) { out.append(' ') }
            out.append("ΔY[$i]")
            repeat((Nb + 1) * VALUE_LENGTH - "ΔY[$i]".length) { out.append(' ') }
            if (i < Nr - 1) {
                out.append("ΔZ[$i]")
                repeat(Nb * VALUE_LENGTH - "ΔZ[$i]".length) { out.append(' ') }
                repeat(keySize(i + 1, Nb, Nk, isSbColumn)) { out.append("   ") }
            }
        }

        out.append("\n")
        for (j in 0..3) {
            repeat(keySize(0, Nb, Nk, isSbColumn) * VALUE_LENGTH) { out.append(' ') }
            for (i in 0 until Nr) {
                for (k in 0 until Nb) {
                    out.append(Console.bg(color(table[i, j, k])) + content(selector(X[i, j, k].Δ), selector(X[i, j, k].free)) + Console.reset())
                }
                out.append("   ")
                for (k in 0 until Nb) {
                    out.append(Console.bg(color(table[i, j, k])) + content(selector(X[i, j, k].Δ), selector(X[i, j, k].freeS)) + Console.reset())
                }
                out.append("   ")
                for (k in 0 until Nb) {
                    out.append(content(selector(Y[i, j, k].Δ), selector(Y[i, j, k].free)) + Console.reset())
                }
                out.append("   ")
                if (i < Nr - 1) {
                    for (k in 0 until Nb) {
                        out.append(content(selector(Z[i, j, k].Δ), selector(Z[i, j, k].free)) + Console.reset())
                    }
                }
                repeat(keySize(i + 1, Nb, Nk, isSbColumn)) { out.append("   ") }
            }
            out.append("\n")
        }

    }

    private fun presentLegend() {
        out.append("Legend: ")
        out.append(Console.bg(color(BoomerangTable.DDT)) + " DDT " + Console.reset())
        out.append(' ')
        out.append(Console.bg(color(BoomerangTable.DDT2)) + " DDT2 " + Console.reset())
        out.append(' ')
        out.append(Console.bg(color(BoomerangTable.BCT)) + " BCT " + Console.reset())
        out.append(' ')
        out.append(Console.bg(color(BoomerangTable.UBCT)) + " UBCT " + Console.reset())
        out.append(' ')
        out.append(Console.bg(color(BoomerangTable.LBCT)) + " LBCT " + Console.reset())
        out.append(' ')
        out.append(Console.bg(color(BoomerangTable.EBCT)) + " EBCT " + Console.reset())
        out.appendLine()
        out.appendLine("======")
    }

    fun present(
        Nr: Int, Nb: Int, Nk: Int, isSbColumn: (Int) -> Boolean,
        X: Tensor3<BoomerangSbVar>, Y: Tensor3<BoomerangVar>, Z: Tensor3<BoomerangVar>, WK: Matrix<OptionalBoomerangSbVar>
    ) {
        val keyTransitions = keyTable(WK)
        val textTransitions = table(X)

        presentLegend()
        presentKey(Nr, Nb, Nk,  TrailVar::upper, WK, keyTransitions, isSbColumn)
        presentText(Nr, Nb, Nk, TrailVar::upper, X, textTransitions, Y, Z, isSbColumn)
        presentText(Nr, Nb, Nk, TrailVar::lower, X, textTransitions, Y, Z, isSbColumn)
        presentKey(Nr, Nb, Nk,  TrailVar::lower, WK, keyTransitions, isSbColumn)
    }

}