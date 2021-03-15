package com.github.rloic.phd.core.cryptography.rijndael.boomerang.singlekey.step1

import com.github.rloic.phd.core.arrays.IntTensor3
import com.github.rloic.phd.core.io.Console
import com.github.rloic.phd.core.io.Console.bg
import com.github.rloic.phd.core.io.Console.color
import java.lang.Appendable

@Suppress("NonAsciiCharacters")
class CommonPlainTextSolutionPresenter(val out: Appendable) {

    fun table(s: String) = color(0xffffff) + bg(0x9a5ea1) + s + Console.reset()
    fun ddt2(s: String) = color(0xffffff) + bg(0x98823c) + s + Console.reset()
    fun zero(s: String) = color(0xbdbdbd) + s + Console.reset()
    fun noTransition(s: String) = color(0x212121) + s + Console.reset()

    fun present(
        Nr: Int, Nb: Int,
        ΔXupper: IntTensor3, freeXupper: IntTensor3,
        ΔXlower: IntTensor3, freeXlower: IntTensor3,
        isTable: IntTensor3, isDDT2: IntTensor3
    ) {
        out.appendLine("Legend: ${table(" 1 ")} = is_table, ${ddt2(" 1 ")} = is_ddt2")
        for (i in 0 until Nr) {
            out.append("ΔX↑$i:".padEnd((Nb + 1) * 3, ' '))
        }
        out.append('\n')
        for (j in 0..3) {
            for (i in 0 until Nr) {
                for (k in 0 until Nb) {
                    if (ΔXupper[i, j, k] == 1) {
                        val content = if (freeXupper[i, j, k] == 1) " F " else " 1 "
                        when {
                            isTable[i, j, k] == 1 -> out.append(table(content))
                            isDDT2[i, j, k] == 1 -> out.append(ddt2(content))
                            else -> out.append(noTransition(content))
                        }

                    } else {
                        out.append(zero(" 0 "))
                    }
                }
                out.append(" | ")
            }
            out.append('\n')
        }
        for (i in 0 until Nr) {
            out.append("X↓$i:".padEnd((Nb + 1) * 3, ' '))
        }
        out.append('\n')
        for (j in 0..3) {
            for (i in 0 until Nr) {
                for (k in 0 until Nb) {
                    if (ΔXlower[i, j, k] == 1) {
                        val content = if (freeXlower[i, j, k] == 1) " F " else " 1 "
                        when {
                            isTable[i, j, k] == 1 -> out.append(table(content))
                            isDDT2[i, j, k] == 1 -> out.append(ddt2(content))
                            else -> out.append(noTransition(content))
                        }
                    } else {
                        out.append(zero(" 0 "))
                    }
                }
                out.append(" | ")
            }
            out.append('\n')
        }
    }

}