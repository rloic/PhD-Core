package com.github.phd.core.mzn

import java.io.File

sealed class MznModel(val value: File) {

    val oznFile get(): File {
        val path = value.absolutePath
        if (!path.endsWith(".mzn")) throw Exception("Invalid mzn file extension")
        return File(path.substring(0, path.length - ".mzn".length) + ".ozn")
    }

    class Optimization(value: File, val objective: MznVariable, val search: OptimizationSearch): MznModel(value)

    class CompleteSearch(value: File, val assignment: (MznSolution) -> Assignment.Complete, val forbid: (Assignment.Complete) -> Unit): MznModel(value)

    class PartialSearch(value: File, val assignment: (MznSolution) -> Assignment.Partial, val forbid: (Assignment.Partial) -> Unit): MznModel(value)

}
