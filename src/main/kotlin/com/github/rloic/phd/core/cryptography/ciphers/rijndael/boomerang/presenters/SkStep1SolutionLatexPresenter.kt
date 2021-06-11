package com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.presenters

import com.github.rloic.phd.core.arrays.Matrix
import com.github.rloic.phd.core.cryptography.attacks.ConfiguredBy
import com.github.rloic.phd.core.cryptography.attacks.boomerang.BoomerangTable
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.SkRijndael
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.solutions.*
import com.github.rloic.phd.core.utils.Presenter
import java.io.Closeable
import java.io.IOException
import java.lang.Appendable

class SkStep1SolutionLatexPresenter<T>(val out: Appendable) : Presenter<T>
        where T : ConfiguredBy<SkRijndael>,
              T : RijndaelBoomerangCipher {

    fun color(t: BoomerangTable?) = when (t) {
        null, BoomerangTable.None -> null
        BoomerangTable.DDT -> "DDT"
        BoomerangTable.BCT -> "BCT"
        BoomerangTable.DDT2 -> "DDT2"
        BoomerangTable.UBCT -> "UBCT"
        BoomerangTable.LBCT -> "LBCT"
        BoomerangTable.EBCT -> "EBCT"
    }

    fun Appendable.appendLinearBlock(
        x: Int,
        y: Int,
        variables: Matrix<BoomerangLinVar>,
        trailSelector: (TrailVar) -> Int,
        name: String? = null,
    ) {
        appendLine("    \\block{$y}{${-x}}{${variables.dim2}}{${variables.dim1}}{")

        if (name != null) {
            appendLine("        \\node at (${variables.dim2 / 2}, 4.3) { $name };")
        }

        for (i in 0 until variables.dim1) {
            for (j in 0 until variables.dim2) {
                val variable = variables[i, j]

                val color: String? = if (trailSelector(variable.free) == 1) "FREE" else null
                val code = if (trailSelector(variable.free) != 1 && trailSelector(variable.Δ) == 1) "1" else ""
                if (color != null) {
                    appendLine("        \\draw[fill=$color] ($j, ${variables.dim1 - i - 1}) rectangle ++(1, 1);")
                }
                appendLine("        \\node at (${j + .5}, ${variables.dim1 - i - .5}) {$ \\mathtt{$code} $};")
            }
        }

        appendLine("    }")
    }

    fun Appendable.appendSbBlock(
        x: Int,
        y: Int,
        variables: Matrix<BoomerangSbVar>,
        trailSelector: (TrailVar) -> Int,
        freeSelector: (BoomerangSbVar) -> TrailVar,
        name: String? = null,
        table: Matrix<BoomerangTable>? = null
    ) {
        appendLine("    \\block{$y}{${-x}}{${variables.dim2}}{${variables.dim1}}{")

        if (name != null) {
            appendLine("        \\node at (${variables.dim2 / 2}, 4.3) { $name };")
        }

        for (i in 0 until variables.dim1) {
            for (j in 0 until variables.dim2) {
                val variable = variables[i, j]
                val freeState = freeSelector(variable)
                val isFree = trailSelector(freeState) == 1
                val value = trailSelector(variable.Δ)

                var color = color(table?.get(i, j))
                var text = ""
                if (isFree) {
                    if (color != null) {
                        color = "FREE!25!$color!75"
                    } else {
                        color = "FREE"
                    }
                } else if (value == 0) {
                    if (color != null) {
                        color = "ZERO!75!$color!75"
                    }
                } else {
                    text = "1"
                }
                if (color != null) {
                    appendLine("        \\draw[fill=$color] ($j, ${variables.dim1 - i - 1}) rectangle ++(1, 1);")
                }
                appendLine("        \\node at (${j + .5}, ${variables.dim1 - i - .5}) {$ \\mathtt{$text} $};")
            }
        }

        appendLine("    }")
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
            out.appendSbBlock(
                (data.config.Nb + 1) * 1,
                21 * i,
                data.X[i],
                TrailVar::upper,
                BoomerangSbVar::free,
                "$ X_{$i}^{\\Uparrow} $",
                data.table(i)
            )
            out.appendSbBlock(
                (data.config.Nb + 1) * 2,
                21 * i,
                data.X[i],
                TrailVar::lower,
                BoomerangSbVar::free,
                "$ X_{$i}^{\\Downarrow} $",
                data.table(i)
            )

            // SB
            out.appendSbBlock(
                (data.config.Nb + 1) * 1,
                21 * i + 5,
                data.X[i],
                TrailVar::upper,
                BoomerangSbVar::freeS,
                "$ SX_{$i}^{\\Uparrow} $",
                data.table(i)
            )
            out.appendSbBlock(
                (data.config.Nb + 1) * 2,
                21 * i + 5,
                data.X[i],
                TrailVar::lower,
                BoomerangSbVar::freeS,
                "$ SX_{$i}^{\\Downarrow} $",
                data.table(i)
            )

            // SR
            out.appendLinearBlock(
                (data.config.Nb + 1) * 1,
                21 * i + 10,
                data.Y[i],
                TrailVar::upper,
                "$ Y_{$i}^{\\Uparrow} $"
            )
            out.appendLinearBlock(
                (data.config.Nb + 1) * 2,
                21 * i + 10,
                data.Y[i],
                TrailVar::lower,
                "$ Y_{$i}^{\\Downarrow} $"
            )

            // MC
            if (i < data.config.Nr - 1) {
                out.appendLinearBlock(
                    (data.config.Nb + 1) * 1,
                    21 * i + 15,
                    data.Z[i],
                    TrailVar::upper,
                    "$ Z_{$i}^{\\Uparrow} $"
                )
                out.appendLinearBlock(
                    (data.config.Nb + 1) * 2,
                    21 * i + 15,
                    data.Z[i],
                    TrailVar::lower,
                    "$ Z_{$i}^{\\Downarrow} $"
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