package com.github.rloic.phd.core.utils

import com.github.rloic.phd.core.io.Console
import java.io.BufferedWriter
import java.io.Closeable
import java.io.FileWriter
import java.io.Writer

var logger = Logger(Logger.Level.OFF, true)

class Logger(
    private val defaultLevel: Level,
    private val scriptMode: Boolean
) : Closeable {

    companion object : FromArgs<Logger> {
        override fun from(args: Map<String, String>) = Logger(
            Level.valueOf(args["Logger.level"] ?: "ON"),
            args["Logger.scriptMode"]?.toBoolean() ?: false
        )
    }

    init {
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                this@Logger.close()
            }
        })
    }

    enum class HeaderMode {
        ANSI, TEXT, NONE
    }

    private class Subscriber<T : Appendable>(
        val implementation: T,
        val headerMode: HeaderMode,
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

    fun addTerminal(level: Level) = logger.addSubscriber(System.out, HeaderMode.ANSI, false, level)
    fun addTerminal() = logger.addSubscriber(System.out, HeaderMode.ANSI, false)
    fun addLogFile(fileName: String) {
        val logWriter = BufferedWriter(FileWriter(fileName))
        logger.addSubscriber(logWriter, HeaderMode.NONE, true)
    }
    fun addLogFile(fileName: String, level: Level) {
        val logWriter = BufferedWriter(FileWriter(fileName))
        logger.addSubscriber(logWriter, HeaderMode.NONE, true, level)
    }

    fun <T : Appendable> addSubscriber(
        subscriber: T,
        header: HeaderMode = HeaderMode.NONE,
        autoCloseIfPossible: Boolean = true,
        overrideLevel: Level? = null
    ) {
        val level = overrideLevel ?: defaultLevel
        if (level == Level.OFF) return
        if (subscribersLevels[level.ordinal - 1].any { it.implementation == subscriber }) {
            return
        }
        subscribersLevels[level.ordinal - 1] += Subscriber(subscriber, if (scriptMode) HeaderMode.NONE else header, autoCloseIfPossible)
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
                subscriber.printHeader(level)
                subscriber.implementation.append(msg)
                subscriber.implementation.append('\n')
                if (subscriber.implementation is Writer) {
                    subscriber.implementation.flush()
                }
            }
        }
    }

    private fun <T: Appendable> Subscriber<T>.printHeader(level: Level) {
        when (headerMode) {
            HeaderMode.ANSI -> { implementation.append(level.ansiMsg()) }
            HeaderMode.TEXT -> { implementation.append(level.msg) }
            HeaderMode.NONE -> {}
        }
        when (headerMode) {
            HeaderMode.NONE -> {}
            else -> implementation.append('\t')
        }
    }

    private fun log(level: Level, fmt: String, arg: Any?, vararg args: Any?) {
        assert(level != Level.OFF)
        for (iLevel in level.ordinal - 1 until Level.ALL.ordinal) {
            for (subscriber in subscribersLevels[iLevel]) {
                subscriber.printHeader(level)
                subscriber.implementation.append(String.format(fmt, arg, *args))
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
                subscriber.printHeader(level)
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

    fun fatal(fmt: String, arg: Any?, vararg args: Any?) = log(Level.FATAL, fmt, arg, *args)
    fun error(fmt: String, arg: Any?, vararg args: Any?) = log(Level.ERROR, fmt, arg, *args)
    fun warn(fmt: String, arg: Any?, vararg args: Any?) = log(Level.WARN, fmt, arg, *args)
    fun info(fmt: String, arg: Any?, vararg args: Any?) = log(Level.INFO, fmt, arg, *args)
    fun debug(fmt: String, arg: Any?, vararg args: Any?) = log(Level.DEBUG, fmt, arg, *args)
    fun trace(fmt: String, arg: Any?, vararg args: Any?) = log(Level.TRACE, fmt, arg, *args)
    fun log(fmt: String, arg: Any?, vararg args: Any?) = log(Level.ON, fmt, arg, *args)

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