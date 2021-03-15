package com.github.rloic.phd.core.cryptography.rijndael.boomerang.relatedkey.step1.optimization

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.arrays.IntTensor3
import com.github.rloic.phd.core.io.Console.color
import com.github.rloic.phd.core.io.Console.reset
import com.github.rloic.phd.core.utils.Presenter

class PlainTextSolutionPresenter(private val out: Appendable) : Presenter<Solution> {

    private val VALUE_LENGTH = " 1 ".length
    private val GREY = color(0xcccccc)

    private fun presentKey(
        Nr: Int, Nb: Int,
        WK: IntMatrix, freeWK: IntMatrix, freeSWK: IntMatrix,
        isTable: IntMatrix, isDDT2: IntMatrix,
        isSbColumn: (Int) -> Boolean
    ) {
        for (j in 0..3) {
            var hasSbox = false
            for (ik in 0 until Nb * (Nr + 1)) {
                if (isSbColumn(ik)) {
                    hasSbox = true
                    if (freeWK[j, ik] == 1) {
                        out.append(" F ")
                    } else {
                        out.append(" ${WK[j, ik]} ")
                    }
                    if (freeSWK[j, ik] == 1) {
                        out.append("(F)")
                    } else {
                        out.append("(${WK[j, ik]})")
                    }
                } else {
                    if (freeWK[j, ik] == 1) {
                        out.append(GREY + " F " + reset())
                    } else {
                        out.append(GREY + " ${WK[j, ik]} " + reset())
                    }
                }

                if (ik % Nb == Nb - 1) {
                    val offset = if (ik / Nb < Nr - 1) 4 else 3
                    repeat(Nb * (offset + 1) - (if(hasSbox) 1 else 0)) { out.append("   ") }
                }

            }
            out.append("\n")
        }

    }

    private fun presentText(
        Nr: Int, Nb: Int,
        X: IntTensor3, freeX: IntTensor3, freeSX: IntTensor3,
        Y: IntTensor3, freeY: IntTensor3,
        Z: IntTensor3, freeZ: IntTensor3,
        isTable: IntTensor3, isDDT2: IntTensor3,
    ) {
        repeat((Nb + 1) * VALUE_LENGTH) { out.append(' ') }
        for (i in 0 until Nr) {
            out.append("ΔX[$i]")
            repeat((Nb + 1) * VALUE_LENGTH - "ΔX[$i]".length) { out.append(' ') }
            out.append("ΔSX[$i]")
            repeat((Nb + 1) * VALUE_LENGTH - "ΔSX[$i]".length) { out.append(' ') }
            out.append("ΔY[$i]")
            repeat((Nb + 1) * VALUE_LENGTH - "ΔY[$i]".length) { out.append(' ') }
            if (i < Nr - 1) {
                out.append("ΔZ[$i]")
                repeat((Nb + 1) * VALUE_LENGTH - "ΔZ[$i]".length) { out.append(' ') }
                repeat(Nb) { out.append("   ") }
            }
        }

        out.append("\n")
        for (j in 0..3) {
            repeat((Nb + 1) * VALUE_LENGTH) { out.append(' ') }
            for (i in 0 until Nr) {
                for (k in 0 until Nb) {
                    if (freeX[i, j, k] == 1) {
                        out.append(" F ")
                    } else {
                        out.append(" ${X[i, j, k]} ")
                    }
                }
                out.append("   ")
                for (k in 0 until Nb) {
                    if (freeSX[i, j, k] == 1) {
                        out.append(" F ")
                    } else {
                        out.append(" ${X[i, j, k]} ")
                    }
                }
                out.append(GREY)
                out.append("   ")
                for (k in 0 until Nb) {
                    if (freeY[i, j, k] == 1) {
                        out.append(" F ")
                    } else {
                        out.append(" ${Y[i, j, k]} ")
                    }

                }
                out.append("   ")
                if (i < Nr - 1) {
                    for (k in 0 until Nb) {
                        if (freeZ[i, j, k] == 1) {
                            out.append(" F ")
                        } else {
                            out.append(" ${Z[i, j, k]} ")
                        }

                    }
                }
                out.append(reset())
                repeat(Nb+1) { out.append("   ") }
            }
            out.append("\n")
        }

    }

    override fun present(data: Solution) {
        val Nr = data.config.Nr
        val Nb = data.config.Nb
        val isSbColumn = data.config::isSbColumn

        presentKey(Nr, Nb, data.ΔWKupper, data.freeWKupper, data.freeSWKupper, data.isTableKey, data.isDDT2Key, isSbColumn)
        presentText(Nr, Nb, data.ΔXupper, data.freeXupper, data.freeSBupper, data.ΔYupper, data.freeYupper, data.ΔZupper, data.freeZupper, data.isTable, data.isDDT2)
        presentText(Nr, Nb, data.ΔXlower, data.freeXlower, data.freeSBlower, data.ΔYlower, data.freeYlower, data.ΔZlower, data.freeZlower, data.isTable, data.isDDT2)
        presentKey(Nr, Nb, data.ΔWKlower, data.freeWKlower, data.freeSWKlower, data.isTableKey, data.isDDT2Key, isSbColumn)
    }
}