package com.github.phd.core.mzn

import java.io.File

interface Mzn2FznCompiler {

    fun format(mznSolution: MznSolution, oznFile: File): MznSolution
    fun compile(mznModel: MznModel, data: Map<String, Any>, solver: SolverKind, vararg mznArgs: String): FznModel

}