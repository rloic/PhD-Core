package com.github.phd.core.utils

interface FromArgs<T> {

    fun from(args: Map<String, String>): T

}

interface FromArgsWith<T, U> {
    fun from(args: Map<String, String>, with: U): T
}