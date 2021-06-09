package com.github.rloic.phd.core.cryptography.ciphers.rijndael.differential.presenters

import com.github.rloic.phd.core.cryptography.ciphers.rijndael.differential.solutions.EnumerateRkStep1Solution
import com.github.rloic.phd.core.utils.Presenter

class EnumerateRkStep1SolutionPresenter(private val out: Appendable) : Presenter<EnumerateRkStep1Solution> {
    override fun present(data: EnumerateRkStep1Solution) {
        for (i in 0 until data.config.Nr) {
            out.append("X$i:".padEnd((data.config.Nb + 1) * 3, ' '))
        }
        out.append('\n')
        for (j in 0..3) {
            for (i in 0 until data.config.Nr) {
                for (k in 0 until data.config.Nb) {
                    if (data.ΔX[i, j, k] == 1) {
                        out.append("1  ")
                    } else {
                        out.append("0  ")
                    }
                }
                out.append(" | ")
            }
            out.append('\n')
        }
        for (i in 0 until data.config.Nr + 1) {
            out.append("WK$i:".padEnd((data.config.Nb + 1) * 3, ' '))
        }
        out.append('\n')
        for (j in 0..3) {
            for (ik in 0 until (data.config.Nr + 1) * data.config.Nb) {
                if (data.config.isSbColumn(ik)) {
                    if (data.ΔWK[j, ik] == 1) {
                        out.append("1  ")
                    } else {
                        out.append("0  ")
                    }
                } else {
                    out.append("_  ")
                }

                if (ik % data.config.Nb == data.config.Nb - 1) {
                    out.append(" | ")
                }
            }
            out.append('\n')
        }
        out.append('\n')
    }
}