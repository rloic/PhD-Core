package com.github.rloic.phd.core.utils

sealed class Option<out T>
object None: Option<Nothing>()
class Some<T>(val value: T): Option<T>()