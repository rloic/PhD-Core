package com.github.rloic.phd.core.cryptography.rijndael.boomerang.relatedkey.step1

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.arrays.IntTensor3
import com.github.rloic.phd.core.arrays.Matrix
import com.github.rloic.phd.core.arrays.Tensor3
import com.github.rloic.phd.core.cryptography.boomerang.BoomerangRules
import com.github.rloic.phd.core.cryptography.boomerang.BoomerangTable
import com.github.rloic.phd.core.io.Console

@Suppress("NonAsciiCharacters")
class CommonPlainTextSolutionPresenter(val out: Appendable) {

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
        DXupper: IntTensor3, freeXupper: IntTensor3, freeSBupper: IntTensor3,
        DXlower: IntTensor3, freeXlower: IntTensor3, freeSBlower: IntTensor3
    ): Tensor3<BoomerangTable> {
        check(listOf(freeXupper, freeSBupper, DXlower, freeXlower, freeSBlower).all { it.dim1 == DXupper.dim1 && it.dim2 == DXupper.dim2 && it.dim3 == DXupper.dim3 })
        return Tensor3(DXupper.dim1, DXupper.dim2, DXupper.dim3) { i, j, k ->
            when {
                BoomerangRules.isDDT(DXupper[i, j, k], freeXupper[i, j, k], freeSBupper[i, j, k], DXlower[i, j, k], freeXlower[i, j, k], freeSBlower[i, j, k]) -> {
                    BoomerangTable.DDT
                }
                BoomerangRules.isDDT2(DXupper[i, j, k], freeXupper[i, j, k], freeSBupper[i, j, k], DXlower[i, j, k], freeXlower[i, j, k], freeSBlower[i, j, k]) -> {
                    BoomerangTable.DDT2
                }
                BoomerangRules.isBCT(DXupper[i, j, k], freeXupper[i, j, k], freeSBupper[i, j, k], DXlower[i, j, k], freeXlower[i, j, k], freeSBlower[i, j, k]) -> {
                    BoomerangTable.BCT
                }
                BoomerangRules.isUBCT(DXupper[i, j, k], freeXupper[i, j, k], freeSBupper[i, j, k], DXlower[i, j, k], freeXlower[i, j, k], freeSBlower[i, j, k]) -> {
                    BoomerangTable.UBCT
                }
                BoomerangRules.isLBCT(DXupper[i, j, k], freeXupper[i, j, k], freeSBupper[i, j, k], DXlower[i, j, k], freeXlower[i, j, k], freeSBlower[i, j, k]) -> {
                    BoomerangTable.LBCT
                }
                BoomerangRules.isEBCT(DXupper[i, j, k], freeXupper[i, j, k], freeSBupper[i, j, k], DXlower[i, j, k], freeXlower[i, j, k], freeSBlower[i, j, k]) -> {
                    BoomerangTable.EBCT
                }
                else -> {
                    BoomerangTable.None
                }
            }
        }
    }

    private fun table(
        DXupper: IntMatrix, freeXupper: IntMatrix, freeSBupper: IntMatrix,
        DXlower: IntMatrix, freeXlower: IntMatrix, freeSBlower: IntMatrix
    ): Matrix<BoomerangTable> {
        check(listOf(freeXupper, freeSBupper, DXlower, freeXlower, freeSBlower).all { it.dim1 == DXupper.dim1 && it.dim2 == DXupper.dim2 })
        return Matrix(DXupper.dim1, DXupper.dim2) { j, k ->
            when {
                BoomerangRules.isDDT(DXupper[j, k], freeXupper[j, k], freeSBupper[j, k], DXlower[j, k], freeXlower[j, k], freeSBlower[j, k]) -> {
                    BoomerangTable.DDT
                }
                BoomerangRules.isDDT2(DXupper[j, k], freeXupper[j, k], freeSBupper[j, k], DXlower[j, k], freeXlower[j, k], freeSBlower[j, k]) -> {
                    BoomerangTable.DDT2
                }
                BoomerangRules.isBCT(DXupper[j, k], freeXupper[j, k], freeSBupper[j, k], DXlower[j, k], freeXlower[j, k], freeSBlower[j, k]) -> {
                    BoomerangTable.BCT
                }
                BoomerangRules.isUBCT(DXupper[j, k], freeXupper[j, k], freeSBupper[j, k], DXlower[j, k], freeXlower[j, k], freeSBlower[j, k]) -> {
                    BoomerangTable.UBCT
                }
                BoomerangRules.isLBCT(DXupper[j, k], freeXupper[j, k], freeSBupper[j, k], DXlower[j, k], freeXlower[j, k], freeSBlower[j, k]) -> {
                    BoomerangTable.LBCT
                }
                BoomerangRules.isEBCT(DXupper[j, k], freeXupper[j, k], freeSBupper[j, k], DXlower[j, k], freeXlower[j, k], freeSBlower[j, k]) -> {
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
        Nr: Int, Nb: Int, Nk: Int,
        WK: IntMatrix, freeWK: IntMatrix, freeSWK: IntMatrix,
        transitions: Matrix<BoomerangTable>,
        isSbColumn: (Int) -> Boolean
    ) {
        for (j in 0..3) {
            for (ik in 0 until Nb * (Nr + 1)) {
                if (isSbColumn(ik)) {
                    out.append(Console.bg(color(transitions[j, ik])) + content(WK[j, ik], freeWK[j, ik]) + Console.reset())
                    out.append(Console.bg(color(transitions[j, ik])) + content(WK[j, ik], freeSWK[j, ik], true) + Console.reset())
                } else {
                    out.append(content(WK[j, ik], freeWK[j, ik]) + Console.reset())
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
        Nr: Int, Nb: Int, Nk: Int, isSbColumn: (Int) -> Boolean,
        X: IntTensor3, freeX: IntTensor3, freeSX: IntTensor3,
        table: Tensor3<BoomerangTable>,
        Y: IntTensor3, freeY: IntTensor3,
        Z: IntTensor3, freeZ: IntTensor3,
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
                    out.append(Console.bg(color(table[i, j, k])) + content(X[i, j, k], freeX[i, j, k]) + Console.reset())
                }
                out.append("   ")
                for (k in 0 until Nb) {
                    out.append(Console.bg(color(table[i, j, k])) + content(X[i, j, k], freeSX[i, j, k]) + Console.reset())
                }
                out.append("   ")
                for (k in 0 until Nb) {
                    out.append(content(Y[i, j, k], freeY[i, j, k]) + Console.reset())
                }
                out.append("   ")
                if (i < Nr - 1) {
                    for (k in 0 until Nb) {
                        out.append(content(Z[i, j, k], freeZ[i, j, k]) + Console.reset())
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
        ΔXupper: IntTensor3, freeXupper: IntTensor3, freeSBupper: IntTensor3,
        ΔYupper: IntTensor3, freeYupper: IntTensor3,
        ΔZupper: IntTensor3, freeZupper: IntTensor3,
        ΔWKupper: IntMatrix, freeWKupper: IntMatrix, freeSWKupper: IntMatrix,
        ΔXlower: IntTensor3, freeXlower: IntTensor3, freeSBlower: IntTensor3,
        ΔYlower: IntTensor3, freeYlower: IntTensor3,
        ΔZlower: IntTensor3, freeZlower: IntTensor3,
        ΔWKlower: IntMatrix, freeWKlower: IntMatrix, freeSWKlower: IntMatrix,
    ) {


        val keyTransitions = table(ΔWKupper, freeWKupper, freeSWKupper, ΔWKlower, freeWKlower, freeSWKlower)
        val textTransitions = table(ΔXupper, freeXupper, freeSBupper, ΔXlower, freeXlower, freeSBlower)

        presentLegend()
        presentKey(Nr, Nb, Nk, ΔWKupper, freeWKupper, freeSWKupper, keyTransitions, isSbColumn)
        presentText(Nr, Nb, Nk, isSbColumn, ΔXupper, freeXupper, freeSBupper, textTransitions, ΔYupper, freeYupper, ΔZupper, freeZupper)
        presentText(Nr, Nb, Nk, isSbColumn, ΔXlower, freeXlower, freeSBlower, textTransitions, ΔYlower, freeYlower, ΔZlower, freeZlower)
        presentKey(Nr, Nb, Nk, ΔWKlower, freeWKlower, freeSWKlower, keyTransitions, isSbColumn)

    }

}