package com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.presenters

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.arrays.Matrix
import com.github.rloic.phd.core.cryptography.attacks.boomerang.BoomerangTable
import com.github.rloic.phd.core.cryptography.attacks.boomerang.BoomerangTable.*
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.solutions.OptimizeRkStep2Solution
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.solutions.OptimizeSkStep2Solution
import com.github.rloic.phd.core.utils.Presenter
import java.io.Closeable
import java.io.IOException

@Suppress("NonAsciiCharacters")
class SkStep2SolutionLatexPresenter(val out: Appendable) : Presenter<OptimizeSkStep2Solution> {

    private fun hexa(n: Int): String {
        if (n == 0) return "\\textcolor{gray}{0x00}"
        if (n == 256) return ""
        val hexString = Integer.toHexString(n)
        var prefix = "0x"
        if (hexString.length == 1) {
            prefix += "0"
        }
        return prefix + hexString.toUpperCase()
    }

    private fun color(t: BoomerangTable?) = when (t) {
        null, None -> null
        DDT -> "DDT"
        BCT -> "BCT"
        DDT2 -> "DDT2"
        UBCT -> "UBCT"
        LBCT -> "LBCT"
        EBCT -> "EBCT"
    }

    fun java.lang.Appendable.appendBlock(
        x: Int,
        y: Int,
        values: IntMatrix,
        name: String? = null,
        table: Matrix<BoomerangTable> ? =null
    ) = appendBlock(x, y, Matrix(values.dim1, values.dim2) { i, j -> values[i, j] as Int? }, name, table)

    fun java.lang.Appendable.appendBlock(
        x: Int,
        y: Int,
        values: Matrix<Int?>,
        name: String? = null,
        table: Matrix<BoomerangTable> ? =null
    ) {
        appendLine("    \\block{$y}{${-x}}{${values.dim2}}{${values.dim1}}{")

        if (name != null) {
            appendLine("        \\node at (${values.dim2 / 2}, 4.3) { $name };")
        }

        for (i in 0 until values.dim1) {
            for (j in 0 until values.dim2) {
                val value = values[i, j]
                if (value != null) {
                    var color = color(table?.get(i, j))
                    if (value == 256) {
                        if (color != null) {
                            color = "FREE!25!$color!75"
                        } else {
                            color = "FREE"
                        }
                    } else if (value == 0) {
                        if (color != null) {
                            color = "ZERO!75!$color!75"
                        }
                    }
                    if (color != null) {
                        appendLine("        \\draw[fill=$color] ($j, ${values.dim1 - i - 1}) rectangle ++(1, 1);")
                    }
                    appendLine("        \\node at (${j + .5}, ${values.dim1 - i - .5}) {$ \\mathtt{${hexa(value)}} $};")
                }

            }
        }

        appendLine("    }")
    }

    override fun present(data: OptimizeSkStep2Solution) {

        out.appendLine("\\documentclass[preview]{standalone}")
        out.appendLine("\\usepackage[utf8]{inputenc}")
        out.appendLine("\\usepackage[T1]{fontenc}")
        out.appendLine("\\usepackage{tikz}")
        out.appendLine("\\usepackage{xcolor}")
        out.appendLine("\\definecolor{DDT}{HTML}{cc554f}")
        out.appendLine("\\definecolor{DDT2}{HTML}{4aad92}")
        out.appendLine("\\definecolor{BCT}{HTML}{c45ca2}")
        out.appendLine("\\definecolor{UBCT}{HTML}{7aa444}")
        out.appendLine("\\definecolor{LBCT}{HTML}{7878cd}")
        out.appendLine("\\definecolor{EBCT}{HTML}{c1883f}")
        out.appendLine("\\definecolor{FREE}{HTML}{9e9e9e}")
        out.appendLine("\\definecolor{ZERO}{HTML}{ffffff}")
        out.appendLine("\\newcommand{\\block}[5]{")
        out.appendLine("	\\begin{scope}[shift={(#1, #2)}]")
        out.appendLine("		#5")
        out.appendLine("		\\draw (0, 0) -- ++(#4, 0) -- ++(0, #3) -- ++({-#4}, 0) -- cycle;")
        out.appendLine("		\\foreach \\i in {1,...,#4}{")
        out.appendLine("			\\draw (\\i, 0) -- ++(0, #3);")
        out.appendLine("		}")
        out.appendLine("		\\foreach \\i in {1,...,#3}{")
        out.appendLine("			\\draw (0, \\i) -- ++(#4, 0);")
        out.appendLine("		}")
        out.appendLine("	\\end{scope}")
        out.appendLine("}")
        out.appendLine("\\begin{document}")
        out.appendLine("	\\begin{tikzpicture}")

        out.appendLine("  \\node[fill=DDT, draw=black, minimum height=1cm,minimum width=1cm] at (-2, -3) {$\\mathtt{DDT}$};")
        out.appendLine("  \\node[fill=DDT2, draw=black, minimum height=1cm,minimum width=1cm] at (-2, -4) {$\\mathtt{DDT2}$};")
        out.appendLine("  \\node[fill=BCT, draw=black, minimum height=1cm,minimum width=1cm] at (-2, -5) {$\\mathtt{BCT}$};")
        out.appendLine("  \\node[fill=UBCT, draw=black, minimum height=1cm,minimum width=1cm] at (-2, -6) {$\\mathtt{UBCT}$};")
        out.appendLine("  \\node[fill=LBCT, draw=black, minimum height=1cm,minimum width=1cm] at (-2, -7) {$\\mathtt{LBCT}$};")
        out.appendLine("  \\node[fill=EBCT, draw=black, minimum height=1cm,minimum width=1cm] at (-2, -8) {$\\mathtt{EBCT}$};")

        for (i in 0 until data.config.Nr) {
            // X
            out.appendBlock((data.config.Nb + 1) * 1, 21 * i, data.δXupper[i], "$ X_{$i}^{\\Uparrow} $", data.table[i])
            out.appendBlock((data.config.Nb + 1) * 2, 21 * i, data.δXlower[i], "$ X_{$i}^{\\Downarrow} $", data.table[i])

            // SB
            out.appendBlock((data.config.Nb + 1) * 1, 21 * i + 5, data.δSXupper[i], "$ SX_{$i}^{\\Uparrow} $", data.table[i])
            out.appendBlock((data.config.Nb + 1) * 2, 21 * i + 5, data.δSXlower[i], "$ SX_{$i}^{\\Downarrow} $", data.table[i])

            // SR
            out.appendBlock((data.config.Nb + 1) * 1, 21 * i + 10, data.δYupper[i], "$ Y_{$i}^{\\Uparrow} $")
            out.appendBlock((data.config.Nb + 1) * 2, 21 * i + 10, data.δYlower[i], "$ Y_{$i}^{\\Downarrow} $")

            // MC
            if (i < data.config.Nr - 1) {
                out.appendBlock((data.config.Nb + 1) * 1, 21 * i + 15, data.δZupper[i], "$ Z_{$i}^{\\Uparrow} $")
                out.appendBlock((data.config.Nb + 1) * 2, 21 * i + 15, data.δZlower[i], "$ Z_{$i}^{\\Downarrow} $")
            }
        }

        out.appendLine("	\\end{tikzpicture}")
        out.appendLine("\\end{document}")
    }

    override fun close() {
        if (out is Closeable) {
            try {
                out.close()
            } catch (ioe: IOException) {
            }
        }
    }
}