package javaextensions.io

import java.io.File
import java.io.IOException
import java.lang.RuntimeException

fun mkdirs(folder: File): File? = try {
    folder.mkdirs()
    folder
} catch (ioe: IOException) {
    null
}

fun mkdirs(path: String): File? = mkdirs(File(path))

operator fun File.div(child: String) = File(this, child)
operator fun <E : Enum<E>> File.div(child: E) = File(this, child.toString())
operator fun File.div(child: Int) = File(this, child.toString())

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