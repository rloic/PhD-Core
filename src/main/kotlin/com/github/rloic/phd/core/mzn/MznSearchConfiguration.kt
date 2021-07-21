package com.github.rloic.phd.core.mzn

interface MznSearchConfiguration {

    fun toMzn(): MznContent

    companion object {
        fun from(decisionVars: PartialMznModel,
                 strategy: SearchStrategy,
                 valueSelector: ValueSelector,) : MznSearchConfiguration {
            return object : MznSearchConfiguration {
                val content = buildString {
                    appendLine("solve :: int_search(")
                    appendLine(decisionVars.file.readText())
                    appendLine("  , ${strategy.mzn}, ${valueSelector.mzn}, complete\n) satisfy;")
                }

                override fun toMzn() = MznContent(content)
            }
        }
    }

}