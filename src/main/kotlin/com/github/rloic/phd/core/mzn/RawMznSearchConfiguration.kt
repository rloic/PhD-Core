package com.github.rloic.phd.core.mzn

class RawMznSearchConfiguration(private val raw: String): MznSearchConfiguration {
    override fun toMzn() = MznContent(raw)
}