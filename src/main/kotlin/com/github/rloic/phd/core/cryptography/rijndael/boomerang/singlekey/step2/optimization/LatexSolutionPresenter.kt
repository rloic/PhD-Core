package com.github.rloic.phd.core.cryptography.rijndael.boomerang.singlekey.step2.optimization

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.arrays.Matrix
import com.github.rloic.phd.core.cryptography.rijndael.boomerang.singlekey.step2.optimization.Solution.Table.*
import com.github.rloic.phd.core.utils.Presenter

class LatexSolutionPresenter(private val out: Appendable) : Presenter<Solution> {

    data class Column(val header: String, val value: IntMatrix, val table: Matrix<Solution.Table>? = null)

    private fun setTTFont() = out.append("\\tt\n")

    private fun colorize(table: Solution.Table?, value: String): String {
        if (value == "0x00") {
            return "\\color{gray}{$value}"
        }

        val color = when (table) {
            None, null -> "white"
            DDT -> "cat1"
            DDT2 -> "cat2"
            BCT -> "cat3"
            UBCT -> "cat4"
            LBCT -> "cat5"
            EBCT -> "cat6"
        }

        return "\\cellcolor{$color}{$value}"
    }

    private fun colorize(table: Solution.Table?, i: Int): String {
        fun hex(i: Int): String {
            if (i == 256) return "FREE"
            if (i == 0) return "0x00"

            val s = Integer.toHexString(i)
            return if (s.length == 1) {
                "0x0$s"
            } else {
                "0x$s"
            }
        }

        return colorize(table, hex(i))
    }

    override fun present(data: Solution) {
        val Nr = data.config.Nr

        header()
        setTTFont()
        legend()
        for (i in 0 until Nr) {
            val upperTrail = mutableListOf(
                Column("$\\delta X^{\\uparrow}_$i$", data.δXupper[i], data.table[i]),
                Column("$\\delta SX^{\\uparrow}_$i$", data.δSXupper[i], data.table[i]),
                Column("$\\delta Y^{\\uparrow}_$i$", data.δYupper[i])
            )
            if (i < Nr - 1) {
                upperTrail += Column( "$\\delta Z^{\\uparrow}_$i$", data.δZupper[i])
            }

            tabular(upperTrail, data.proba[i], this::colorize)

            val lowerTrail  = mutableListOf(
                Column("$\\delta X^{\\downarrow}_$i$", data.δXlower[i], data.table[i]),
                Column("\$\\delta SX^{\\downarrow}_$i\$", data.δSXlower[i], data.table[i]),
                Column("$\\delta Y^{\\downarrow}_$i\$", data.δYlower[i])
            )
            if (i < Nr - 1) {
                lowerTrail += Column( "$\\delta Z^{\\downarrow}_$i\$", data.δZlower[i])
            }

            tabular(lowerTrail, data.proba[i], this::colorize)
        }
        footer()
    }

    private fun header() {
        out.append("""
            \documentclass[a4paper,landscape]{article}
            \usepackage[nomarginpar, margin=5mm]{geometry}
            \usepackage[utf8]{inputenc}
            \usepackage[table]{xcolor}
            \usepackage{inconsolata}
                        
            \definecolor{cat1}{HTML}{6ac9ca}
            \definecolor{cat2}{HTML}{dc97ae}
            \definecolor{cat3}{HTML}{89bf95}
            \definecolor{cat4}{HTML}{95abdc}
            \definecolor{cat5}{HTML}{d6d89d}
            \definecolor{cat6}{HTML}{caa475}
            
            \begin{document}
            \centering
        """.trimIndent())
    }

    private fun footer() {
        out.append("""
            \end{document}
        """.trimIndent())
    }

    private fun legend() {
        out.append("\\begin{tabular}{llll}")

        out.append(colorize(null, 0) + "&: inactive byte")
        out.append(" & ")
        out.append(colorize(null, "0x??") + "&: active byte (no sbox)")
        out.append("\\\\\n")

        out.append(colorize(DDT, "0x??") + "&: DDT Transition")
        out.append(" & ")
        out.append(colorize(DDT2, "0x??") + "&: DDT2 Transition")
        out.append("\\\\\n")

        out.append(colorize(BCT, "0x??") + "&: BCT Transition")
        out.append(" & ")
        out.append(colorize(UBCT, "0x??") + "&: UBCT Transition")
        out.append("\\\\\n")

        out.append(colorize(LBCT, "0x??") + "&: LBCT Transition")
        out.append(" & ")
        out.append(colorize(EBCT, "0x??") + "&: EBCT Transition")
        out.append("\\\\\n")

        out.append("\\end{tabular}\\\\ \\vspace{.5cm} \n")
    }

    private fun tabular(
        columns: List<Column>,
        proba: Matrix<Int?>,
        fn: (Solution.Table?, Int) -> String) {
        out.append("\\begin{tabular}{")
        for (i in 0 until columns.size) {
            out.append('|')
            repeat(columns[i].value.dim2) { out.append('c') }
            if (i == columns.size - 1) {
                out.append('|')
            }
        }
        out.append("}\n")
        out.append("\\hline\n")
        for ((i, col) in columns.withIndex()) {
            val nbCols = col.value.dim2
            val leftBorder = if (i == 0) "|" else ""
            val rightBorder = if (i == columns.size - 1) "|" else ""
            out.append("\\multicolumn{$nbCols}{${leftBorder}c${rightBorder}}{${col.header}}")
            if (i < columns.size - 1) {
                out.append(" & ")
            }
        }

        out.append("\\\\ \\hline\n")

        val maxRows = columns.map(Column::value).maxOf(IntMatrix::dim1)
        for (i in 0 until maxRows) {
            for ((k, col) in columns.withIndex()) {
                val matrix = col.value
                val table = col.table
                for (j in 0 until matrix.dim2) {
                    if (i < matrix.dim1) {
                        out.append(fn(table?.get(i, j), matrix[i, j]))
                    }
                    if (j < matrix.dim2 - 1 || k < columns.size - 1) {
                        out.append(" & ")
                    } else {
                        out.append("\\\\ \n")
                    }
                }
            }
        }

        out.append("\\hline\n")
        val allCols = columns.map(Column::value).sumOf { it.dim2 }
        val probaSum = proba.deepFlatten().filterNotNull().sum()
        out.append("\\multicolumn{$allCols}{|c|}{$2^{-$probaSum}$}\\\\\\hline\n")
        out.append("\\end{tabular}\\\\ \\vspace{.5cm} \n")
        out.append("\n")
    }

    override fun close() {
        if (out is AutoCloseable) { out.close() }
    }
}