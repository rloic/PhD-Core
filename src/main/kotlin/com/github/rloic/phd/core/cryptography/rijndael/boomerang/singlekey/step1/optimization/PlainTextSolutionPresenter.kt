package com.github.rloic.phd.core.cryptography.rijndael.boomerang.singlekey.step1.optimization

import com.github.rloic.phd.core.utils.Presenter
import java.lang.Appendable

class PlainTextSolutionPresenter(private val out: Appendable): Presenter<Solution> {
    override fun present(data: Solution) {
        for (i in 0 until data.config.Nr) {
            out.append("X↑$i:".padEnd((data.config.Nb + 1) * 3, ' '))
        }
        out.append('\n')
        for (j in 0..3) {
            for (i in 0 until data.config.Nr) {
                for (k in 0 until data.config.Nb) {
                    if (data.ΔXupper[i, j, k] == 1) {
                        out.append("1  ")
                    } else {
                        out.append("0  ")
                    }
                }
                out.append(" | ")
            }
            out.append('\n')
        }
        for (i in 0 until data.config.Nr) {
            out.append("X↓$i:".padEnd((data.config.Nb + 1) * 3, ' '))
        }
        out.append('\n')
        for (j in 0..3) {
            for (i in 0 until data.config.Nr) {
                for (k in 0 until data.config.Nb) {
                    if (data.ΔXlower[i, j, k] == 1) {
                        out.append("1  ")
                    } else {
                        out.append("0  ")
                    }
                }
                out.append(" | ")
            }
            out.append('\n')
        }
    }
}