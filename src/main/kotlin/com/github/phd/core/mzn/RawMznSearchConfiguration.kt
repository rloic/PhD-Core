package com.github.phd.core.mzn

class RawMznSearchConfiguration(private val raw: String): MznSearchConfiguration {
    override fun toMzn() = MznContent(raw)
}