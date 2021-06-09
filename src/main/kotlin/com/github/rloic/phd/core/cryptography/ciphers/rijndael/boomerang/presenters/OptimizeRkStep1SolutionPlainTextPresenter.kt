package com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.presenters

import com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.solutions.OptimizeRijndaelBoomerangRkStep1Solution
import com.github.rloic.phd.core.utils.Presenter

class OptimizeRkStep1SolutionPlainTextPresenter(out: Appendable) : Presenter<OptimizeRijndaelBoomerangRkStep1Solution> {

    private val delegate = CommonRkStep1PlainTextSolutionPresenter(out)

    override fun present(data: OptimizeRijndaelBoomerangRkStep1Solution) {
        delegate.present(
            data.config.Nr, data.config.Nb, data.config.Nk, data.config::isSbColumn,
            data.X, data.Y, data.Z, data.WK
        )
    }
}