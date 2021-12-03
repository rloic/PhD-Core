package com.github.phd.core.cryptography.ciphers.rijndael.boomerang.presenters

import com.github.phd.core.arrays.Matrix
import com.github.phd.core.cryptography.attacks.ConfiguredBy
import com.github.phd.core.cryptography.attacks.boomerang.BoomerangTable
import com.github.phd.core.cryptography.ciphers.rijndael.RkRijndael
import com.github.phd.core.cryptography.ciphers.rijndael.boomerang.solutions.*
import com.github.phd.core.utils.Presenter
import java.io.Closeable
import java.io.IOException
import java.lang.Appendable

class RkStep1SolutionLatexPresenter<T>(val out: Appendable) : Presenter<T>
    where T: ConfiguredBy<RkRijndael>,
          T: RijndaelBoomerangCipherWithKeySchedule
{

    private fun Appendable.block(_1: Int, _2: Int, _3: Int, _4: Int, _6: String = "", _5: Appendable.() -> Unit) {
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

    private fun color(t: BoomerangTable?) = when (t) {
        null, BoomerangTable.None -> null
        BoomerangTable.DDT -> "DDT"
        BoomerangTable.BCT -> "BCT"
        BoomerangTable.DDT2 -> "DDT2"
        BoomerangTable.UBCT -> "UBCT"
        BoomerangTable.LBCT -> "LBCT"
        BoomerangTable.EBCT -> "EBCT"
    }

    fun Appendable.appendLinearBlock(
        y: Int,
        x: Int,
        variables: Matrix<BoomerangLinVar>,
        trailSelector: (TrailVar) -> Int,
        name: String? = null,
    ) {
        block(x, -y, variables.dim1, variables.dim2) {
            if (name != null) {
                appendLine("        \\node at (${variables.dim2 / 2}, 4.3) { $name };")
            }

            for (i in 0 until variables.dim1) {
                for (j in 0 until variables.dim2) {
                    val variable = variables[i, j]

                    var color: String? = null
                    var code = ""

                    if (trailSelector(variable.free) == 1) {
                        color = "FREE"
                    } else {
                        if (trailSelector(variable.Δ) == 1) {
                            code = "X"
                        } else {
                            color = "ZERO"
                        }
                    }

                    if (color != null) {
                        appendLine("        \\draw[fill=$color] ($j, ${variables.dim1 - i - 1}) rectangle ++(1, 1);")
                    }
                    appendLine("        \\node at (${j + .5}, ${variables.dim1 - i - .5}) {$ \\mathtt{$code} $};")
                }
            }
        }
    }

    fun Appendable.appendSbBlock(
        y: Int,
        x: Int,
        variables: Matrix<BoomerangSbVar>,
        trailSelector: (TrailVar) -> Int,
        freeSelector: (BoomerangSbVar) -> TrailVar,
        name: String? = null,
        table: Matrix<BoomerangTable>? = null
    ) {
        block(x, -y, variables.dim1, variables.dim2) {
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
                        } else {
                            color = "ZERO"
                        }
                    } else {
                        text = "X"
                    }

                    if (color != null) {
                        appendLine("        \\draw[fill=$color] ($j, ${variables.dim1 - i - 1}) rectangle ++(1, 1);")
                    }
                    appendLine("        \\node at (${j + .5}, ${variables.dim1 - i - .5}) {$ \\mathtt{$text} $};")
                }
            }
        }
    }

    fun Appendable.appendKey(
        y: Int,
        x: Int,
        variables: Matrix<BoomerangOptionalSbVar>,
        trailSelector: (TrailVar) -> Int,
        freeSelector: (BoomerangOptionalSbVar) -> TrailVar?,
        name: String? = null,
        table: Matrix<BoomerangTable>? = null
    ) {
        block(x, -y, variables.dim1, variables.dim2, "dotted") {
            if (name != null) {
                appendLine("        \\node at (${variables.dim2 / 2}, 4.3) { $name };")
            }

            for (i in 0 until variables.dim1) {
                for (j in 0 until variables.dim2) {
                    val variable = variables[i, j]
                    val freeState = freeSelector(variable)
                    if (freeState != null) {
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
                            } else {
                                color = "ZERO"
                            }
                        } else {
                            text = "X"
                        }

                        if (color != null) {
                            appendLine("        \\draw[fill=$color] ($j, ${variables.dim1 - i - 1}) rectangle ++(1, 1);")
                        }
                        appendLine("        \\draw ($j, ${variables.dim1 - i - 1}) rectangle ++(1, 1);")
                        appendLine("        \\node at (${j + .5}, ${variables.dim1 - i - .5}) {$ \\mathtt{$text} $};")
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

        out.appendKey(UPPER_KEY, -SHIFT + 1, data.subKey(0), TrailVar::upper, BoomerangOptionalSbVar::free, "$ RK_{0}^{\\Uparrow} $", data.subKeyTable(0))
        out.appendKey(UPPER_KEY, 1, data.subKey(0), TrailVar::upper, BoomerangOptionalSbVar::freeS, "$ SRK_{0}^{\\Uparrow} $", data.subKeyTable(0))

        out.appendKey(LOWER_KEY, -SHIFT + 1, data.subKey(0), TrailVar::lower, BoomerangOptionalSbVar::free, "$ RK_{0}^{\\Downarrow} $", data.subKeyTable(0))
        out.appendKey(LOWER_KEY, 1, data.subKey(0), TrailVar::lower, BoomerangOptionalSbVar::freeS, "$ SRK_{0}^{\\Downarrow} $", data.subKeyTable(0))

        for (i in 0 until data.config.Nr) {
            // X
            out.appendSbBlock(UPPER_TRAIL, ROUND_WIDTH * i, data.X[i], TrailVar::upper, BoomerangSbVar::free, "$ X_{$i}^{\\Uparrow} $", data.table(i))
            out.appendSbBlock(LOWER_TRAIL, ROUND_WIDTH * i, data.X[i], TrailVar::lower, BoomerangSbVar::free, "$ X_{$i}^{\\Downarrow} $", data.table(i))

            // SB
            out.appendSbBlock(UPPER_TRAIL, ROUND_WIDTH * i + SHIFT, data.X[i], TrailVar::upper, BoomerangSbVar::freeS, "$ SX_{$i}^{\\Uparrow} $", data.table(i))
            out.appendSbBlock(LOWER_TRAIL, ROUND_WIDTH * i + SHIFT, data.X[i], TrailVar::lower, BoomerangSbVar::freeS, "$ SX_{$i}^{\\Downarrow} $", data.table(i))

            // SR
            out.appendLinearBlock(UPPER_TRAIL, ROUND_WIDTH * i + 2 * SHIFT, data.Y[i], TrailVar::upper, "$ Y_{$i}^{\\Uparrow} $")
            out.appendLinearBlock(LOWER_TRAIL, ROUND_WIDTH * i + 2 * SHIFT, data.Y[i], TrailVar::lower, "$ Y_{$i}^{\\Downarrow} $")

            // MC
            if (i < data.config.Nr - 1) {
                out.appendLinearBlock(UPPER_TRAIL, ROUND_WIDTH * i + 3 * SHIFT, data.Z[i], TrailVar::upper, "$ Z_{$i}^{\\Uparrow} $")
                out.appendLinearBlock(LOWER_TRAIL, ROUND_WIDTH * i + 3 * SHIFT, data.Z[i], TrailVar::lower, "$ Z_{$i}^{\\Downarrow} $")

                out.appendKey(UPPER_KEY, ROUND_WIDTH * (i + 1) - SHIFT + 1, data.subKey(i + 1), TrailVar::upper, BoomerangOptionalSbVar::free, "$ RK_{${i + 1}}^{\\Uparrow} $", data.subKeyTable(i + 1))
                out.appendKey(UPPER_KEY, ROUND_WIDTH * (i + 1) + 1, data.subKey(i + 1), TrailVar::upper, BoomerangOptionalSbVar::freeS, "$ SRK_{${i + 1}}^{\\Uparrow} $", data.subKeyTable(i + 1))

                out.appendKey(LOWER_KEY, ROUND_WIDTH * (i + 1) - SHIFT + 1, data.subKey(i + 1), TrailVar::lower , BoomerangOptionalSbVar::free, "$ RK_{${i + 1}}^{\\Downarrow} $", data.subKeyTable(i + 1))
                out.appendKey(LOWER_KEY, ROUND_WIDTH * (i + 1) + 1, data.subKey(i + 1), TrailVar::lower, BoomerangOptionalSbVar::freeS, "$ SRK_{${i + 1}}^{\\Downarrow} $", data.subKeyTable(i + 1))
            } else {
                out.appendKey(UPPER_KEY, ROUND_WIDTH * (i + 1) - 2 * SHIFT + 1, data.subKey(i + 1), TrailVar::upper, BoomerangOptionalSbVar::free, "$ RK_{${i + 1}}^{\\Uparrow} $", data.subKeyTable(i + 1))
                out.appendKey(UPPER_KEY, ROUND_WIDTH * (i + 1) - SHIFT + 1, data.subKey(i + 1), TrailVar::upper, BoomerangOptionalSbVar::freeS, "$ SRK_{${i + 1}}^{\\Uparrow} $", data.subKeyTable(i + 1))

                out.appendKey(LOWER_KEY, ROUND_WIDTH * (i + 1) - 2 * SHIFT + 1, data.subKey(i + 1), TrailVar::lower, BoomerangOptionalSbVar::free, "$ RK_{${i + 1}}^{\\Downarrow} $", data.subKeyTable(i + 1))
                out.appendKey(LOWER_KEY, ROUND_WIDTH * (i + 1) - SHIFT + 1, data.subKey(i + 1), TrailVar::lower, BoomerangOptionalSbVar::freeS, "$ SRK_{${i + 1}}^{\\Downarrow} $", data.subKeyTable(i + 1))
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