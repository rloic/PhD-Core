package com.github.phd.core.utils

interface Presenter<T> {

    fun present(data: T)

    fun close() {}

}