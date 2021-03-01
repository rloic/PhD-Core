package com.github.rloic.phd.core.utils

interface Parser<In, Out> {

    fun parse(serialized: In): Out

}