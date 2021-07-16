package com.github.rloic.phd.core.io

object Console {

    val SUCC = success(" SUCC  ", fg = true, bg = true)
    val FATAL = fatal(" FATAL ", true, true)
    val WARN = warn(" WARN  ", fg = true, bg = true)
    val ERR = error(" ERROR ", fg = true, bg = true)
    val INFO = info(" INFO  ", fg = true, bg = true)
    val DEBUG = debug(" DEBUG ", fg = true, bg = true)
    val TRACE = trace(" TRACE ", fg = true, bg = true)

    fun success(s: String, fg: Boolean = true, bg: Boolean = false) = buildString {
        if (fg) {
            append(color(0x3c763d))
        }
        if (bg) {
            append(bg(0xdff0d8))
        }
        append(s)
        append(reset())
    }

    fun fatal(s: String, fg: Boolean = true, bg: Boolean = false) = buildString {
        if (fg) {
            append(color(0xa94442))
        }
        if (bg) {
            append(bg(0xf2dede))
        }
        append(s)
        append(reset())
    }

    fun error(s: String, fg: Boolean = true, bg: Boolean = false) = buildString {
        if (fg) {
            append(color(0xa94442))
        }
        if (bg) {
            append(bg(0xf2dede))
        }
        append(s)
        append(reset())
    }

    fun warn(s: String, fg: Boolean = true, bg: Boolean = false) = buildString {
        if (fg) {
            append(color(0x8a6d3b))
        }
        if (bg) {
            append(bg(0xfcf8e3))
        }
        append(s)
        append(reset())
    }

    fun info(s: String, fg: Boolean = true, bg: Boolean = false) = buildString {
        if (fg) {
            append(color(0x31708f))
        }
        if (bg) {
            append(bg(0xd9edf7))
        }
        append(s)
        append(reset())
    }

    fun debug(s: String, fg: Boolean = true, bg: Boolean = false) = buildString {
        if (fg) {
            append(color(0))
        }
        if (bg) {
            append(color(0))
        }
        append(s)
        append(reset())
    }

    fun trace(s: String, fg: Boolean = true, bg: Boolean = false) = buildString {
        if (fg) {
            append(color(0))
        }
        if (bg) {
            append(color(0))
        }
        append(s)
        append(reset())
    }

    private fun hexaToRgb(hexa: Int) = intArrayOf(
        (hexa shr 16) and 0xFF,
        (hexa shr 8) and 0xFF,
        (hexa shr 0) and 0xFF,
    )

    fun color(hexa: Int): String {
        val (r, g, b) = hexaToRgb(hexa)
        return color(r, g, b)
    }

    fun color(r: Int, g: Int, b: Int) = "\u001B[38;2;${r};${g};${b}m"

    fun bg(hexa: Int): String {
        val (r, g, b) = hexaToRgb(hexa)
        return bg(r, g, b)
    }

    fun bg(r: Int, g: Int, b: Int) = "\u001B[48;2;${r};${g};${b}m"

    fun reset() = "\u001B[0m"

}