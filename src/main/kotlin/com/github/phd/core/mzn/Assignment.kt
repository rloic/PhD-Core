package com.github.phd.core.mzn

sealed class Assignment(val content: String) {

    class Complete(content: String): Assignment(content)
    class Partial(content: String): Assignment(content)

}

