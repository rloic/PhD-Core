package com.github.rloic.phd.core.cryptography.rijndael.differential.relatedkey.step2.optimization

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.arrays.Matrix
import com.github.rloic.phd.core.utils.Presenter
import java.io.Closeable
import java.io.IOException
import java.lang.Math.abs

class LatexCharacteristicDistinguisherPresenter(val out: Appendable) : Presenter<Solution> {

    private infix fun IntMatrix.xor(other: IntMatrix) = IntMatrix(dim1, dim2) { i,j -> this[i, j] xor other[i,j] }

    private fun inlineContent(matrix: IntMatrix) = buildString {
        append("\\texttt{")
        for (j in 0 until matrix.dim1) {
            for (k in 0 until matrix.dim2) {
                val hexa = Integer.toHexString(matrix[j, k])
                if (hexa.length == 1) {
                    append('0')
                }
                append(hexa.toUpperCase())
            }
            append(' ')
        }
        setLength(length - 1)
        append('}')
    }

    private fun inlineProba(matrix: IntMatrix) = buildString {
        var nb6 = 0
        var nb7 = 0

        for (i in 0 until matrix.dim1) {
            for (j in 0 until matrix.dim2) {
                if (abs(matrix[i, j]) == 6) {
                    nb6 += 1
                }
                if (abs(matrix[i, j]) == 7) {
                    nb7 += 1
                }
            }
        }

        if (nb6 == 0 && nb7 == 0) {
            append('-')
        } else {
            append("$2^{")
            if (nb6 > 1) { append(nb6); append(" \\times (-6)") }
            if (nb6 == 1) append("-6")
            if (nb7 > 1) { append(nb7); append(" \\times (-7)") }
            if (nb7 == 1) append("-7")
            append("}$")
        }

    }

    private fun inlineProba(matrix: Matrix<Int?>) = buildString {
        var nb6 = 0
        var nb7 = 0

        for (i in 0 until matrix.dim1) {
            for (j in 0 until matrix.dim2) {
                val p = matrix[i, j] ?: continue
                if (abs(p) == 6) {
                    nb6 += 1
                }
                if (abs(p) == 7) {
                    nb7 += 1
                }
            }
        }

        if (nb6 == 0 && nb7 == 0) {
            append('-')
        } else {
            append("$2^{")
            if (nb6 > 1) { append(nb6); append(" \\times (-6)") }
            if (nb6 == 1) append("-6")
            if (nb7 > 1) { append(nb7); append(" \\times (-7)") }
            if (nb7 == 1) append("-7")
            append("}$")
        }

    }

    private fun Appendable.appendLatexLine(underline: Boolean, vararg elements: Any?) {
        if (elements.isNotEmpty()) {
            append(elements[0].toString())

            for (i in 1 until elements.size) {
                append(" & ")
                append(elements[i].toString())
            }

            append("\\\\")
            if (underline) {
                appendLine("\\hline")
            } else {
                appendLine()
            }
        }
    }

    override fun present(data: Solution) {

        out.appendLine("\\documentclass[preview]{standalone}")
        out.appendLine("\\usepackage[utf8]{inputenc}")
        out.appendLine("\\usepackage[T1]{fontenc}")
        out.appendLine("\\begin{document}")
        out.appendLine("\\begin{tabular}{|c||c|c|c|c|}")

        out.appendLine("""
    \hline
    Round & ${'$'}\delta X_i = X_i\oplus X'_i${'$'} (before \texttt{SBOX}) & ${'$'}\delta RK_i${'$'}  &  Pr(States) & Pr(Key) \\ 
          &  ${'$'}\delta SBX_i${'$'}   (after \texttt{SBOX})              &   &                           & \\\hline
        """.trimIndent())

        val plainText = data.δX[0] xor data.subKey(0)
        out.appendLatexLine(true, "$ i = 0 $", inlineContent(plainText), inlineContent(data.subKey(0)), "-", "-")

        for (i in 0 until data.config.Nr) {
            out.appendLatexLine(false, if(i == 0) "$ i = ${i + 1} $" else "$ ${i + 1} $", inlineContent(data.δX[i]), inlineContent(data.subKey(i + 1)), inlineProba(data.p[i]), inlineProba(data.subKeyProba(i + 1)))
            out.appendLatexLine(true, "         ", inlineContent(data.δSX[i]), "", "", "")
        }

        val cipher = data.δY[data.config.Nr - 1] xor data.subKey(data.config.Nr)
        out.appendLatexLine(true , "output", inlineContent(cipher), inlineContent(data.subKey(data.config.Nr)), "", "")

        out.appendLine("	\\end{tabular}")
        out.appendLine("\\end{document}")
    }

    override fun close() {
        if (out is Closeable) {
            try { out.close() } catch (ioe: IOException) {}
        }
    }
}