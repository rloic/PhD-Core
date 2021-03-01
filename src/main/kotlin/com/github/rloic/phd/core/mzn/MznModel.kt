package com.github.rloic.phd.core.mzn

import java.io.File

sealed class MznModel(val value: File) {

    class Optimization(value: File, val objective: MznVariable, val search: OptimizationSearch): MznModel(value)

    class CompleteSearch(value: File, val assignment: (MznSolution) -> Assignment.Complete, val forbid: (Assignment.Complete) -> Unit): MznModel(value)

    class PartialSearch(value: File, val assignment: (MznSolution) -> Assignment.Partial, val forbid: (Assignment.Partial) -> Unit): MznModel(value)

}
