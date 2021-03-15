package com.github.rloic.phd.core.cryptography.rijndael.boomerang.singlekey.step2.optimization

import com.github.rloic.phd.core.io.Console.bg
import com.github.rloic.phd.core.io.Console.color
import com.github.rloic.phd.core.io.Console.reset
import com.github.rloic.phd.core.utils.Presenter

class PlainTextSolutionPresenter(private val out: Appendable) : Presenter<Solution> {

    private fun lightGrey(s: String) = "\u001B[37m$s\u001B[0m"

    private fun format(n: Int): String {
        if (n !in 0..255) return "FREE"
        val hexStr = Integer.toHexString(n)
        return if (hexStr.length == 1) "0x0$hexStr"
        else "0x$hexStr"
    }

    private fun color(table: Solution.Table, strValue: String): String {
        val colorCode = when(table) {
            Solution.Table.None -> ""
            Solution.Table.DDT -> color(0xffffff) + bg(0x846bca)
            Solution.Table.DDT2 -> color(0xffffff) + bg(0x64a757)
            Solution.Table.BCT -> color(0xffffff) + bg(0xc65d9e)
            Solution.Table.UBCT -> color(0xffffff) + bg(0xb7923f)
            Solution.Table.LBCT -> color(0xffffff) + bg(0x57a2cc)
            Solution.Table.EBCT -> color(0xffffff) + bg(0xcb584c)
        }
        return colorCode + strValue + reset()
    }

    private fun formatLine(data: Solution, end: Boolean = false, tableSelector: ((Solution) -> List<Solution.Table>)? = null, rowSelector: (Solution) -> IntArray) {
        val line = rowSelector(data)
        val table = tableSelector?.invoke(data)
        for (k in 0 until data.config.Nb) {
            if (line[k] != 0) {
                if (table == null) {
                    out.append(color(Solution.Table.None, format(line[k]) + "  "))
                } else {
                    out.append(color(table[k], format(line[k])) + "  ")
                }
            } else {
                out.append(lightGrey("0x00  "))
            }
        }
        if (!end) out.append(" | ")
    }

    override fun present(data: Solution) {

        val Nb = data.config.Nb
        val Nr = data.config.Nr

        fun margin(header: String) = header.padEnd((Nb + 1) * 5 + 3, ' ')

        out.appendLine("Legend:")
        out.append(lightGrey("0x00") + ": Inactive byte        ")
        out.appendLine(color(Solution.Table.None, "0x??") + ": No sbox transition")
        out.append(color(Solution.Table.DDT, "0x??") + ": DDT Transition       ")
        out.appendLine(color(Solution.Table.DDT2, "0x??") + ": DDT2 transition")
        out.append(color(Solution.Table.BCT, "0x??") + ": BCT Transition       ")
        out.appendLine(color(Solution.Table.UBCT, "0x??") + ": UBCT transition")
        out.append(color(Solution.Table.LBCT, "0x??") + ": LBCT Transition      ")
        out.appendLine(color(Solution.Table.EBCT, "0x??") + ": EBCT transition")
        out.appendLine()

        for (i in 0 until Nr) {
            out.appendLine("Round probability: 2^{-" + data.proba[i].deepFlatten().sumBy { it ?: 0 } + "}")
            out.append(margin("δX↑$i:"))
            out.append(margin("δSX↑$i:"))
            out.append(margin("δY↑$i:"))
            if (i != Nr - 1) out.append("δZ↑$i:")

            out.append('\n')
            for (j in 0..3) {
                formatLine(data, tableSelector = { d -> d.table[i, j] }) { d -> d.δXupper[i, j] }
                formatLine(data, tableSelector = { d -> d.table[i, j] }) { d -> d.δSXupper[i, j] }
                formatLine(data, end = i == Nr - 1) { d -> d.δYupper[i, j] }
                if (i != Nr - 1) {
                    formatLine(data, end = true) { d -> d.δZupper[i, j] }
                }
                out.append('\n')
            }

            out.appendLine()
            out.append(margin("δX↓$i:"))
            out.append(margin("δSX↓$i:"))
            out.append(margin("δY↓$i:"))
            if (i != Nr - 1) out.append("δZ↓$i:")

            out.append('\n')
            for (j in 0..3) {
                formatLine(data, tableSelector = { d -> d.table[i, j] }) { d -> d.δXlower[i, j] }
                formatLine(data, tableSelector = { d -> d.table[i, j] }) { d -> d.δSXlower[i, j] }
                formatLine(data, end = i == Nr - 1) { d -> d.δYlower[i, j] }
                if (i != Nr - 1) {
                    formatLine(data, end = true) { d -> d.δZlower[i, j] }
                }
                out.append('\n')
            }
            out.appendLine('\n')
        }
    }
}