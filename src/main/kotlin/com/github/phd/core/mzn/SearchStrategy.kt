package com.github.phd.core.mzn

enum class SearchStrategy(internal val mzn: String) {
    InputOrder("input_order"),
    FirstFail("first_fail"),
    Smallest("smallest"),
    DomOverWDeg("dom_w_deg")
}