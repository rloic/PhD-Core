package javaextensions.io

import java.io.File
import java.lang.RuntimeException

operator fun File.div(child: String) = File(this, child)
operator fun <E: Enum<E>> File.div(child: E) = File(this, child.toString())
operator fun  File.div(child: Int) = File(this, child.toString())

fun File.concat(other: File) {

    other.bufferedReader().useLines { lines ->
        for (line in lines) {
            this.appendText(line)
            this.appendText("\n")
        }
    }

}

fun mkDirs(path: String): File {
    val file = File(path)

    if (!file.exists()) {
        file.mkdirs()
    } else {
        if (!file.isDirectory) {
            throw RuntimeException("$file is not a directory")
        }
    }

    return file
}