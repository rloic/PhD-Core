package com.github.rloic.phd.core.cryptography.rijndael.boomerang.singlekey.step1.enumeration

import com.github.rloic.phd.core.utils.Presenter

import com.github.rloic.phd.core.cryptography.rijndael.boomerang.singlekey.step1.CommonPlainTextSolutionPresenter

class PlainTextSolutionPresenter(out: Appendable) : Presenter<Solution> {

    private val delegate = CommonPlainTextSolutionPresenter(out)

    override fun present(data: Solution) {
        delegate.present(
            data.config.Nr, data.config.Nb,
            data.ΔXupper, data.freeXupper,
            data.ΔXlower, data.freeXlower
        )
    }
}