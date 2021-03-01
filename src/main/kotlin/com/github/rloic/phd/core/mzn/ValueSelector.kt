package com.github.rloic.phd.core.mzn

enum class ValueSelector(internal val mzn: String) {

    InDomainMin("indomain_min"),
    InDomainMedian("indomain_median"),
    InDomainRandom("indomain_random"),
    InDomainSplit("indomain_split")

}