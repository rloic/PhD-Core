package com.github.phd.core.utils

interface Parser<In, Out> {

    fun parse(serialized: In): Out

}