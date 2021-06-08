package com.github.rloic.phd.core.cryptography.rijndael.boomerang.relatedkey.step1.optimization

import com.github.rloic.phd.core.cryptography.rijndael.boomerang.relatedkey.step1.CommonPlainTextSolutionPresenter
import com.github.rloic.phd.core.utils.Presenter

class PlainTextSolutionPresenter(out: Appendable) : Presenter<Solution> {

    private val delegate = CommonPlainTextSolutionPresenter(out)

    override fun present(data: Solution) {
        delegate.present(
            data.config.Nr, data.config.Nb, data.config.Nk, data.config::isSbColumn,
            data.ΔXupper, data.freeXupper, data.freeSBupper,
            data.ΔYupper, data.freeYupper,
            data.ΔZupper, data.freeZupper,
            data.ΔWKupper, data.freeWKupper, data.freeSWKupper,
            data.ΔXlower, data.freeXlower, data.freeSBlower,
            data.ΔYlower, data.freeYlower,
            data.ΔZlower, data.freeZlower,
            data.ΔWKlower, data.freeWKlower, data.freeSWKlower
        )
    }
}