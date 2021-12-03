package com.github.phd.core.mzn

import kotlin.text.StringBuilder

data class MznContent(internal val raw: String)

fun mznContent(fn: StringBuilder.() -> Unit) = MznContent(StringBuilder().apply(fn).toString())