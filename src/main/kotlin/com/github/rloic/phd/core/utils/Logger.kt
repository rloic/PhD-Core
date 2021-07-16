package com.github.rloic.phd.core.utils

import com.github.rloic.phd.core.io.Console
import java.lang.Appendable
import java.lang.Exception

var logger = Logger(Logger.Level.OFF)

class Logger(private val level: Level): AutoCloseable {

    companion object: FromArgs<Logger> {
        override fun from(args: Map<String, String>) = Logger(Level.valueOf(args["Logger.level"] ?: "ON"))
    }

    private data class Subscriber<T: Appendable>(
        val implementation: T,
        val acceptAnsiColors: Boolean = false,
        val autoCloseIfPossible: Boolean = true
    )

    enum class Level(val msg: String, private val fgColor: Int) : Comparable<Level> {
        OFF("", 0),
        ON(" LOG   ", 0x9e9e9e),
        FATAL(" FATAL ", 0xb71c1c),
        ERROR(" ERROR ", 0xff7043),
        WARN( " WARN  ", 0xffa726),
        DEBUG(" DEBUG ", 0x00acc1),
        INFO( " INFO  ", 0x2196f3),
        TRACE(" TRACE ", 0x66bb6a),
        ALL("", 0);

        fun ascii() = buildString {
            append(Console.color(fgColor))
            append(msg)
            append(Console.reset())
        }

    }

    private val subscribers = mutableListOf<Subscriber<Appendable>>()

    fun <T: Appendable> addSubscriber(subscriber: T, useAsciiDisplay: Boolean = false, autoCloseIfPossible: Boolean = true) {
        if (subscribers.all { it.implementation != subscriber }) {
            subscribers.add(Subscriber(subscriber, useAsciiDisplay, autoCloseIfPossible))
        }
    }

    fun removeSubscriber(subscriber: Appendable) {
        subscribers.removeIf { it.implementation == subscriber }
    }

    private fun log(level: Level, msg: String) {
        if (level != Level.OFF && level <= this.level) {
            for (subscriber in subscribers) {
                if (subscriber.acceptAnsiColors) {
                    subscriber.implementation.append(level.ascii())
                } else {
                    subscriber.implementation.append(level.msg)
                }
                subscriber.implementation.append('\t')
                subscriber.implementation.append(msg)
                subscriber.implementation.append('\n')
            }
        }
    }


    private fun log(level: Level, msg: () -> Any?) {
        if (level != Level.OFF && level <= this.level) {
            for (subscriber in subscribers) {
                if (subscriber.acceptAnsiColors) {
                    subscriber.implementation.append(level.ascii())
                } else {
                    subscriber.implementation.append(level.msg)
                }
                subscriber.implementation.append('\t')
                subscriber.implementation.append(msg().toString())
                subscriber.implementation.append('\n')
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

    fun fatal(msg: () -> Any?) = log(Level.FATAL, msg)
    fun error(msg: () -> Any?) = log(Level.ERROR, msg)
    fun warn(msg: () -> Any?) = log(Level.WARN, msg)
    fun info(msg: () -> Any?) = log(Level.INFO, msg)
    fun debug(msg: () -> Any?) = log(Level.DEBUG, msg)
    fun trace(msg: () -> Any?) = log(Level.TRACE, msg)
    fun log(msg: () -> Any?) = log(Level.ON, msg)

    override fun close() {
        for (subscriber in subscribers) {
            if (subscriber.implementation is AutoCloseable && subscriber.autoCloseIfPossible) {
                try { subscriber.implementation.close() }
                catch (e: Exception) {}
            }
        }
    }
}