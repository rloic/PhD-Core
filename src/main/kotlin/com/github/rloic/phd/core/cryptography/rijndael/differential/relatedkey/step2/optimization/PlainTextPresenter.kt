package com.github.rloic.phd.core.cryptography.rijndael.differential.relatedkey.step2.optimization

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.arrays.IntTensor3
import com.github.rloic.phd.core.utils.Presenter

@Suppress("NonAsciiCharacters", "LocalVariableName")
class PlainTextPresenter(private val out: Appendable) : Presenter<Solution> {

    companion object {
        const val indent: Int = 2
    }

    private val leftSpaces = buildString { repeat(indent) { append(' ') } }

    private fun Int.toHexString(pad: Int = 0): String =
        "0x" + if (pad == 0) {
            Integer.toBinaryString(this).toUpperCase()
        } else {
            Integer.toHexString(this).padStart(pad, '0').toUpperCase()
        }

    private fun display(name: String, block: IntMatrix) {
        out.append(leftSpaces)
        out.appendLine(name)
        for (j in 0 until block.dim1) {
            out.appendLine(leftSpaces)
            for (k in 0 until block.dim2) {
                out.append(block[j, k].toHexString(2))
                out.append(" ")
            }
            out.appendLine()
        }
        out.appendLine()
    }

    private fun displayPair(leftName: String, leftBlock: IntMatrix, rightName: String, rightBlock: IntMatrix) {
        check(leftBlock.dim1 == rightBlock.dim1)
        out.append(leftSpaces)
        out.append(leftName.padEnd(leftBlock.dim2 * 5))
        out.append('\t')
        out.appendLine(rightName)
        for (j in 0 until leftBlock.dim1) {
            out.append(leftSpaces)
            for (k in 0 until leftBlock.dim2) {
                out.append(leftBlock[j, k].toHexString(2))
                out.append(" ")
            }
            out.append("\t")
            for (k in 0 until rightBlock.dim2) {
                out.append(rightBlock[j, k].toHexString(2))
                out.append(" ")
            }
            out.appendLine()
        }
        out.appendLine()
    }

    override fun present(data: Solution) {
        val Nr = data.config.Nr
        val Nb = data.config.Nb

        val plainText = IntMatrix(4, Nb) { j, k -> data.δX[0, j, k] xor data.δWK[j, k] }
        val δK = IntTensor3(Nr, 4, Nb) { i, j, k -> data.δWK[j, i * Nb + k] }

        displayPair("δPlainText:", plainText, "δK[0]:", δK[0])

        for (i in 0 until Nr - 1) {
            display("δX[$i]:", data.δX[i])
            display("δSX[$i]:", data.δSX[i])
            display("δY[$i]:", data.δY[i])
            displayPair("δZ[$i]:", data.δZ[i], "δK[${i + 1}]:", δK[i + 1])
        }

        display("δX[${Nr - 1}]:", data.δX[Nr - 1])
        display("δSX[${Nr - 1}]:", data.δSX[Nr - 1])
        display("δY[${Nr - 1}]:", data.δY[Nr - 1])
    }
}