package com.github.rloic.phd.core.mzn

enum class OptimizationSearch {
    Minimize, Maximize;
    override fun toString() = name.lowercase()
}