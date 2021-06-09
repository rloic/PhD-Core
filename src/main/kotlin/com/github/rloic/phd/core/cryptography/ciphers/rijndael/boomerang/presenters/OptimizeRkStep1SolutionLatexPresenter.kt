package com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.presenters

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.solutions.OptimizeRijndaelBoomerangRkStep1Solution
import com.github.rloic.phd.core.utils.Presenter
import java.io.Closeable
import java.io.IOException
import java.lang.Appendable

class OptimizeRkStep1SolutionLatexPresenter(val out: Appendable) : Presenter<OptimizeRijndaelBoomerangRkStep1Solution> {

    fun hexa(n: Int): String = if (n != 0) n.toString() else ""

    fun Appendable.appendBlock(
        block: IntMatrix,
        x: Int,
        y: Int,
        name: String? = null,
        table: IntMatrix? = null,
        ddt2: IntMatrix? = null,
        free: IntMatrix? = null,
    ) {
        appendLine("    \\block{$x}{${-y}}{${block.dim2}}{${block.dim1}}{")

        if (name != null) {
            appendLine("        \\node at (${block.dim2 / 2 + .5}, 4.3) { $name };")
        }

        for (i in 0 until block.dim1) {
            for (j in 0 until block.dim2) {

                val color = when {
                    free?.get(i, j) == 1 -> {
                        "grey"
                    }
                    ddt2?.get(i, j) == 1 -> {
                        "blue"
                    }
                    table?.get(i, j) == 1 -> {
                        "green"
                    }
                    else -> {
                        null
                    }
                }
                if (color != null) {
                    appendLine("        \\draw[fill=$color] (${(block.dim2 - 1 - j)}, $i) rectangle ++(1, 1);")
                }
                appendLine("        \\node at (${(block.dim2 - j) - .5}, ${i + .5}) { \\texttt{${hexa(block[i, j])}} };")
            }
        }

        appendLine("    }")
    }

    override fun present(data: OptimizeRijndaelBoomerangRkStep1Solution) {

        out.appendLine("\\documentclass[preview]{standalone}")
        out.appendLine("\\usepackage[utf8]{inputenc}")
        out.appendLine("\\usepackage[T1]{fontenc}")
        out.appendLine("\\usepackage{libertine}")
        out.appendLine("\\usepackage{libertinust1math}")
        out.appendLine("\\usepackage{tikz}")
        out.appendLine("\\usepackage{xcolor}")
        out.appendLine("\\definecolor{red}{HTML}{f44336}")
        out.appendLine("\\definecolor{pink}{HTML}{e91e63}")
        out.appendLine("\\definecolor{purple}{HTML}{9c27b0}")
        out.appendLine("\\definecolor{deepPurple}{HTML}{673ab7}")
        out.appendLine("\\definecolor{indigo}{HTML}{3f51b5}")
        out.appendLine("\\definecolor{blue}{HTML}{2196f3}")
        out.appendLine("\\definecolor{lightBlue}{HTML}{03a9f4}")
        out.appendLine("\\definecolor{cyan}{HTML}{00bcd4}")
        out.appendLine("\\definecolor{teal}{HTML}{009688}")
        out.appendLine("\\definecolor{green}{HTML}{4caf50}")
        out.appendLine("\\definecolor{lightGreen}{HTML}{8bc34a}")
        out.appendLine("\\definecolor{lime}{HTML}{cddc39}")
        out.appendLine("\\definecolor{yellow}{HTML}{ffeb3b}")
        out.appendLine("\\definecolor{amber}{HTML}{ffc107}")
        out.appendLine("\\definecolor{orange}{HTML}{ff9800}")
        out.appendLine("\\definecolor{deepOrange}{HTML}{ff5722}")
        out.appendLine("\\definecolor{brown}{HTML}{795548}")
        out.appendLine("\\definecolor{grey}{HTML}{9e9e9e}")
        out.appendLine("\\definecolor{blueGrey}{HTML}{607d8b}")
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