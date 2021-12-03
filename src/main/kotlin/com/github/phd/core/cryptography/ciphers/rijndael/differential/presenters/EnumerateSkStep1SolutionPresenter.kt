package com.github.phd.core.cryptography.ciphers.rijndael.differential.presenters

import com.github.phd.core.cryptography.ciphers.rijndael.differential.solutions.EnumerateSkStep1Solution
import com.github.phd.core.utils.Presenter
import java.lang.Appendable

class EnumerateSkStep1SolutionPresenter(private val out: Appendable) : Presenter<EnumerateSkStep1Solution> {
    override fun present(data: EnumerateSkStep1Solution) {
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
        out.append('\n')
    }
}