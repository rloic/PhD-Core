package com.github.rloic.phd.core.utils

import com.github.rloic.phd.core.io.Console
import java.io.Closeable
import java.io.Writer
import java.lang.Appendable
import java.lang.Exception

var logger = Logger(Logger.Level.OFF)

class Logger(private val defaultLevel: Level) : Closeable {

    companion object : FromArgs<Logger> {
        override fun from(args: Map<String, String>) = Logger(Level.valueOf(args["Logger.level"] ?: "ON"))
    }

    init {
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                this@Logger.close()
            }
        })
    }

    private data class Subscriber<T : Appendable>(
        val implementation: T,
        val acceptAnsiColors: Boolean,
        val autoCloseIfPossible: Boolean
    )

    enum class Level(val msg: String, private val fgColor: Int) : Comparable<Level> {
        OFF("", 0),
        ON(" LOG   ", 0x9e9e9e),
        FATAL(" FATAL ", 0xb71c1c),
        ERROR(" ERROR ", 0xff7043),
        WARN(" WARN  ", 0xffa726),
        DEBUG(" DEBUG ", 0x00acc1),
        INFO(" INFO  ", 0x2196f3),
        TRACE(" TRACE ", 0x66bb6a),
        ALL("", 0);

        fun ansiMsg() = buildString {
            append(Console.color(fgColor))
            append(msg)
            append(Console.reset())
        }

    }

    private val subscribersLevels = MutableList(8) { mutableListOf<Subscriber<Appendable>>() }

    fun <T : Appendable> addSubscriber(
        subscriber: T,
        acceptAnsiColors: Boolean = false,
        autoCloseIfPossible: Boolean = true,
        overrideLevel: Level? = null
    ) {
        val level = overrideLevel ?: defaultLevel
        if (level == Level.OFF) return
        if (subscribersLevels[level.ordinal - 1].any { it.implementation == subscriber }) {
            return
        }
        subscribersLevels[level.ordinal - 1] += Subscriber(subscriber, acceptAnsiColors, autoCloseIfPossible)
        assert(subscribersLevels.flatten().count { it.implementation == subscriber } == 1)
    }

    fun removeSubscriber(subscriber: Appendable) {
        for (subscribers in subscribersLevels) {
            subscribers.removeIf { it.implementation == subscriber }
        }
    }

    private fun log(level: Level, msg: String) {
        assert(level != Level.OFF)
        for (iLevel in level.ordinal - 1 until Level.ALL.ordinal) {
            for (subscriber in subscribersLevels[iLevel]) {
                if (subscriber.acceptAnsiColors) {
                    subscriber.implementation.append(level.ansiMsg())
                } else {
                    subscriber.implementation.append(level.msg)
                }
                subscriber.implementation.append('\t')
                subscriber.implementation.append(msg)
                subscriber.implementation.append('\n')
                if (subscriber.implementation is Writer) {
                    subscriber.implementation.flush()
                }
            }
        }
    }

    private fun log(level: Level, fmt: String, vararg args: Any?) {
        assert(level != Level.OFF)
        for (iLevel in level.ordinal - 1 until Level.ALL.ordinal) {
            for (subscriber in subscribersLevels[iLevel]) {
                if (subscriber.acceptAnsiColors) {
                    subscriber.implementation.append(level.ansiMsg())
                } else {
                    subscriber.implementation.append(level.msg)
                }
                subscriber.implementation.append('\t')
                subscriber.implementation.append(String.format(fmt, *args))
                subscriber.implementation.append('\n')
                if (subscriber.implementation is Writer) {
                    subscriber.implementation.flush()
                }
            }
        }
    }

    private fun log(level: Level, msg: () -> Any?) {
        assert(level != Level.OFF)
        for (iLevel in level.ordinal - 1 until Level.ALL.ordinal) {
            for (subscriber in subscribersLevels[iLevel]) {
                if (subscriber.acceptAnsiColors) {
                    subscriber.implementation.append(level.ansiMsg())
                } else {
                    subscriber.implementation.append(level.msg)
                }
                subscriber.implementation.append('\t')
                subscriber.implementation.append(msg().toString())
                subscriber.implementation.append('\n')
                if (subscriber.implementation is Writer) {
                    subscriber.implementation.flush()
                }
            }
        }
    }

    fun fatal(msg: Any?) = log(Level.FATAL, msg.toString())
    fun error(msg: Any?) = log(Level.ERROR, msg.toString())
    fun warn(msg: Any?) = log(Level.WARN, msg.toString())
    fun info(msg: Any?) = log(Level.INFO, msg.toString())
    fun debug(msg: Any?) = log(Level.DEBUG, msg.toString())
    fun trace(msg: Any?) = log(Level.TRACE, msg.toString())
    fun log(msg: Any?) = log(Level.ON, msg.toString())

    fun fatal(fmt: String, vararg args: Any?) = log(Level.FATAL, fmt, *args)
    fun error(fmt: String, vararg args: Any?) = log(Level.ERROR, fmt, *args)
    fun warn(fmt: String, vararg args: Any?) = log(Level.WARN, fmt, *args)
    fun info(fmt: String, vararg args: Any?) = log(Level.INFO, fmt, *args)
    fun debug(fmt: String, vararg args: Any?) = log(Level.DEBUG, fmt, *args)
    fun trace(fmt: String, vararg args: Any?) = log(Level.TRACE, fmt, *args)
    fun log(fmt: String, vararg args: Any?) = log(Level.ON, fmt, *args)

    fun fatal(msg: () -> Any?) = log(Level.FATAL, msg)
    fun error(msg: () -> Any?) = log(Level.ERROR, msg)
    fun warn(msg: () -> Any?) = log(Level.WARN, msg)
    fun info(msg: () -> Any?) = log(Level.INFO, msg)
    fun debug(msg: () -> Any?) = log(Level.DEBUG, msg)
    fun trace(msg: () -> Any?) = log(Level.TRACE, msg)
    fun log(msg: () -> Any?) = log(Level.ON, msg)

    override fun close() {
        for (subscribers in subscribersLevels) {
            for (subscriber in subscribers) {
                if (subscriber.implementation is AutoCloseable && subscriber.autoCloseIfPossible) {
                    try {
                        subscriber.implementation.close()
                    } catch (e: Exception) {
                    }
                }
            }
        }
    }
}