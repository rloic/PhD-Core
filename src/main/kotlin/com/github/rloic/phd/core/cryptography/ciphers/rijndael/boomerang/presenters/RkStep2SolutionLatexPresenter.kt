package com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.presenters

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.arrays.Matrix
import com.github.rloic.phd.core.cryptography.attacks.ConfiguredBy
import com.github.rloic.phd.core.cryptography.attacks.boomerang.BoomerangTable
import com.github.rloic.phd.core.cryptography.attacks.boomerang.BoomerangTable.*
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.RkRijndael
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.solutions.*
import com.github.rloic.phd.core.utils.Presenter
import java.io.Closeable
import java.io.IOException

@Suppress("NonAsciiCharacters")
class RkStep2SolutionLatexPresenter<T>(val out: Appendable) : Presenter<T>
        where T : ConfiguredBy<RkRijndael>,
              T : Step2RijndaelBoomerangCipherWithKeySchedule {

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

    private fun Appendable.block(_1: Int, _2: Int, _3: Int, _4: Int, _6: String = "", _5: java.lang.Appendable.() -> Unit) {
        appendLine("	\\begin{scope}[shift={($_1, $_2)}]")
        _5()
        appendLine("		\\draw[$_6] (0, 0) -- ++($_4, 0) -- ++(0, $_3) -- ++({-$_4}, 0) -- cycle;")
        appendLine("		\\foreach \\i in {1,...,$_4}{")
        appendLine("			\\draw[$_6] (\\i, 0) -- ++(0, $_3);")
        appendLine("		}")
        appendLine("		\\foreach \\i in {1,...,$_3}{")
        appendLine("			\\draw[$_6] (0, \\i) -- ++($_4, 0);")
        appendLine("		}")
        appendLine("	\\end{scope}")
    }

    fun java.lang.Appendable.appendBlock(
        y: Int,
        x: Int,
        values: IntMatrix,
        name: String? = null,
        table: Matrix<BoomerangTable>? = null
    ) = appendBlock(y, x, Matrix(values.dim1, values.dim2) { i, j -> values[i, j] as Int? }, name, table)

    fun java.lang.Appendable.appendBlock(
        y: Int,
        x: Int,
        values: Matrix<Int?>,
        name: String? = null,
        table: Matrix<BoomerangTable>? = null
    ) {
        block(x, -y, values.dim1, values.dim2) {
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
        }
    }

    override fun present(data: T) {

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
        out.appendLine("\\begin{document}")
        out.appendLine("	\\begin{tikzpicture}")

        out.appendLine("  \\node[fill=DDT, draw=black, minimum height=1cm,minimum width=1cm] at (-2, -3) {$\\mathtt{DDT}$};")
        out.appendLine("  \\node[fill=DDT2, draw=black, minimum height=1cm,minimum width=1cm] at (-2, -4) {$\\mathtt{DDT2}$};")
        out.appendLine("  \\node[fill=BCT, draw=black, minimum height=1cm,minimum width=1cm] at (-2, -5) {$\\mathtt{BCT}$};")
        out.appendLine("  \\node[fill=UBCT, draw=black, minimum height=1cm,minimum width=1cm] at (-2, -6) {$\\mathtt{UBCT}$};")
        out.appendLine("  \\node[fill=LBCT, draw=black, minimum height=1cm,minimum width=1cm] at (-2, -7) {$\\mathtt{LBCT}$};")
        out.appendLine("  \\node[fill=EBCT, draw=black, minimum height=1cm,minimum width=1cm] at (-2, -8) {$\\mathtt{EBCT}$};")

        val UPPER_KEY = 0
        val UPPER_TRAIL = 5
        val LOWER_TRAIL = 10
        val LOWER_KEY = 15

        val SHIFT = data.config.Nb + 1
        val ROUND_WIDTH = (SHIFT * 4 + 1)

        out.appendBlock(
            UPPER_KEY, -SHIFT + 1,
            data.subKeyUpper(0),
            "$ RK_{0}^{\\Uparrow} $",
            data.subKeyTable(0)
        )
        out.appendBlock(
            UPPER_KEY, 1,
            data.subSKeyUpper(0),
            "$ SRK_{0}^{\\Uparrow} $",
            data.subKeyTable(0)
        )

        out.appendBlock(
            LOWER_KEY, -SHIFT + 1,
            data.subKeyLower(0),
            "$ RK_{0}^{\\Downarrow} $",
            data.subKeyTable(0)
        )
        out.appendBlock(
            LOWER_KEY, 1,
            data.subSKeyLower(0),
            "$ SRK_{0}^{\\Downarrow} $",
            data.subKeyTable(0)
        )


        for (i in 0 until data.config.Nr) {
            // X
            out.appendBlock(UPPER_TRAIL, ROUND_WIDTH * i, data.δXupper[i], "$ X_{$i}^{\\Uparrow} $", data.table[i])
            out.appendBlock(
                LOWER_TRAIL, ROUND_WIDTH * i,
                data.δXlower[i],
                "$ X_{$i}^{\\Downarrow} $",
                data.table[i]
            )

            // SB
            out.appendBlock(
                UPPER_TRAIL, ROUND_WIDTH * i + SHIFT,
                data.δSXupper[i],
                "$ SX_{$i}^{\\Uparrow} $",
                data.table[i]
            )
            out.appendBlock(
                LOWER_TRAIL, ROUND_WIDTH * i + SHIFT,
                data.δSXlower[i],
                "$ SX_{$i}^{\\Downarrow} $",
                data.table[i]
            )

            // SR
            out.appendBlock(UPPER_TRAIL, ROUND_WIDTH * i + 2 * SHIFT, data.δYupper[i], "$ Y_{$i}^{\\Uparrow} $")
            out.appendBlock(LOWER_TRAIL, ROUND_WIDTH * i + 2 * SHIFT, data.δYlower[i], "$ Y_{$i}^{\\Downarrow} $")

            // MC
            if (i < data.config.Nr - 1) {
                out.appendBlock(UPPER_TRAIL, ROUND_WIDTH * i + 3 * SHIFT, data.δZupper[i], "$ Z_{$i}^{\\Uparrow} $")
                out.appendBlock(LOWER_TRAIL, ROUND_WIDTH * i + 3 * SHIFT, data.δZlower[i], "$ Z_{$i}^{\\Downarrow} $")

                out.appendBlock(
                    UPPER_KEY, ROUND_WIDTH * (i + 1) - SHIFT + 1,
                    data.subKeyUpper(i + 1),
                    "$ RK_{${i + 1}}^{\\Uparrow} $",
                    data.subKeyTable(i + 1)
                )
                out.appendBlock(
                    UPPER_KEY, ROUND_WIDTH * (i + 1) + 1,
                    data.subSKeyUpper(i + 1),
                    "$ SRK_{${i + 1}}^{\\Uparrow} $",
                    data.subKeyTable(i + 1)
                )

                out.appendBlock(
                    LOWER_KEY, ROUND_WIDTH * (i + 1) - SHIFT + 1,
                    data.subKeyLower(i + 1),
                    "$ RK_{${i + 1}}^{\\Downarrow} $",
                    data.subKeyTable(i + 1)
                )
                out.appendBlock(
                    LOWER_KEY, ROUND_WIDTH * (i + 1) + 1,
                    data.subSKeyLower(i + 1),
                    "$ SRK_{${i + 1}}^{\\Downarrow} $",
                    data.subKeyTable(i + 1)
                )
            } else {
                out.appendBlock(
                    UPPER_KEY, ROUND_WIDTH * (i + 1) - 2 * SHIFT + 1,
                    data.subKeyUpper(i + 1),
                    "$ RK_{${i + 1}}^{\\Uparrow} $",
                    data.subKeyTable(i + 1)
                )
                out.appendBlock(
                    UPPER_KEY, ROUND_WIDTH * (i + 1) - SHIFT + 1,
                    data.subSKeyUpper(i + 1),
                    "$ SRK_{${i + 1}}^{\\Uparrow} $",
                    data.subKeyTable(i + 1)
                )

                out.appendBlock(
                    LOWER_KEY, ROUND_WIDTH * (i + 1) - 2 * SHIFT + 1,
                    data.subKeyLower(i + 1),
                    "$ RK_{${i + 1}}^{\\Downarrow} $",
                    data.subKeyTable(i + 1)
                )
                out.appendBlock(
                    LOWER_KEY, ROUND_WIDTH * (i + 1) - SHIFT + 1,
                    data.subSKeyLower(i + 1),
                    "$ SRK_{${i + 1}}^{\\Downarrow} $",
                    data.subKeyTable(i + 1)
                )
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