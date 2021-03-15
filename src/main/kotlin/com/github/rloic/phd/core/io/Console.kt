package com.github.rloic.phd.core.io

object Console {

    val SUCC = "${success(" SUCC ", fg = true, bg = true)}"
    val INFO = "${info(" INFO ", fg = true, bg = true)}"
    val WARN = "${warn(" WARN ", fg = true, bg = true)}"
    val ERR = "${error(" ERR  ", fg = true, bg = true)}"

    fun success(s: String, fg: Boolean = true, bg: Boolean = false) =
        (if (fg) color(0x3c763d) else "") + (if (bg) bg(0xdff0d8) else "") + s + reset()

    fun error(s: String, fg: Boolean = true, bg: Boolean = false) =
        (if (fg) color(0xa94442) else "") + (if (bg) bg(0xf2dede) else "") + s + reset()

    fun warn(s: String, fg: Boolean = true, bg: Boolean = false) =
        (if (fg) color(0x8a6d3b) else "") + (if (bg) bg(0xfcf8e3) else "") + s + reset()

    fun info(s: String, fg: Boolean = true, bg: Boolean = false) =
        (if (fg) color(0x31708f) else "") + (if (bg) bg(0xd9edf7) else "") + s + reset()

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