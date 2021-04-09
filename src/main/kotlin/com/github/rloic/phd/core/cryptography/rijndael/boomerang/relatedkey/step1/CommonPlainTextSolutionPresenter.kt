package com.github.rloic.phd.core.cryptography.rijndael.boomerang.relatedkey.step1

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.arrays.IntTensor3
import com.github.rloic.phd.core.io.Console

@Suppress("NonAsciiCharacters")
class CommonPlainTextSolutionPresenter(val out: Appendable) {

    fun table(s: String) = Console.color(0xffffff) + Console.bg(0x9a5ea1) + s + Console.reset()
    fun ddt2(s: String) = Console.color(0xffffff) + Console.bg(0x98823c) + s + Console.reset()
    fun zero(s: String) = Console.color(0x9e9e9e) + s + Console.reset()
    fun noTransition(s: String) = Console.color(0x212121) + s + Console.reset()
    fun noSbox(s: String) = Console.color(0x9e9e9e) + s + Console.reset()
    val VALUE_LENGTH = 3

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
        isTable: IntMatrix, isDDT2: IntMatrix,
        isSbColumn: (Int) -> Boolean
    ) {
        for (j in 0..3) {
            var hasSbox = 0
            for (ik in 0 until Nb * (Nr + 1)) {
                if (isSbColumn(ik)) {
                    hasSbox += 1

                    if (WK[j, ik] == 1) {
                        val content = if (freeWK[j, ik] == 1) " F " else " 1 "
                        when {
                            isDDT2[j, ik] == 1 -> out.append(ddt2(content))
                            isTable[j, ik] == 1 -> out.append(table(content))
                            else -> out.append(noTransition(content))
                        }
                    } else {
                        out.append(zero(" 0 "))
                    }

                    if (WK[j, ik] == 1) {
                        val content = if (freeSWK[j, ik] == 1) "(F)" else "(1)"
                        when {
                            isDDT2[j, ik] == 1 -> out.append(ddt2(content))
                            isTable[j, ik] == 1 -> out.append(table(content))
                            else -> out.append(noTransition(content))
                        }
                    } else {
                        out.append(zero("(0)"))
                    }
                } else {
                    if (freeWK[j, ik] == 1) {
                        out.append(noSbox(" F "))
                    } else {
                        out.append(noSbox(" ${WK[j, ik]} "))
                    }
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
        Y: IntTensor3, freeY: IntTensor3,
        Z: IntTensor3, freeZ: IntTensor3,
        isTable: IntTensor3, isDDT2: IntTensor3,
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
                    if (X[i, j, k] == 1) {
                        val content = if (freeX[i, j, k] == 1) " F " else " 1 "
                        when {
                            isDDT2[i, j, k] == 1 -> out.append(ddt2(content))
                            isTable[i, j, k] == 1 -> out.append(table(content))
                            else -> out.append(noTransition(content))
                        }
                    } else {
                        out.append(zero(" 0 "))
                    }
                }
                out.append("   ")
                for (k in 0 until Nb) {
                    if (X[i, j, k] == 1) {
                        val content = if (freeSX[i, j, k] == 1) " F " else " 1 "
                        when {
                            isDDT2[i, j, k] == 1 -> out.append(ddt2(content))
                            isTable[i, j, k] == 1 -> out.append(table(content))
                            else -> out.append(noTransition(content))
                        }
                    } else {
                        out.append(zero(" 0 "))
                    }
                }
                out.append("   ")
                for (k in 0 until Nb) {
                    if (freeY[i, j, k] == 1) {
                        out.append(noSbox(" F "))
                    } else {
                        out.append(noSbox(" ${Y[i, j, k]} "))
                    }

                }
                out.append("   ")
                if (i < Nr - 1) {
                    for (k in 0 until Nb) {
                        if (freeZ[i, j, k] == 1) {
                            out.append(noSbox(" F "))
                        } else {
                            out.append(noSbox(" ${Z[i, j, k]} "))
                        }

                    }
                }
                repeat(keySize(i + 1, Nb, Nk, isSbColumn)) { out.append("   ") }
            }
            out.append("\n")
        }

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
        isTable: IntTensor3, isDDT2: IntTensor3,
        isTableKey: IntMatrix, isDDT2Key: IntMatrix
    ) {
        presentKey(Nr, Nb, Nk, ΔWKupper, freeWKupper, freeSWKupper, isTableKey, isDDT2Key, isSbColumn)
        presentText(Nr, Nb, Nk, isSbColumn, ΔXupper, freeXupper, freeSBupper, ΔYupper, freeYupper, ΔZupper, freeZupper, isTable, isDDT2)
        presentText(Nr, Nb, Nk, isSbColumn, ΔXlower, freeXlower, freeSBlower, ΔYlower, freeYlower, ΔZlower, freeZlower, isTable, isDDT2)
        presentKey(Nr, Nb, Nk, ΔWKlower, freeWKlower, freeSWKlower, isTableKey, isDDT2Key, isSbColumn)
    }

}