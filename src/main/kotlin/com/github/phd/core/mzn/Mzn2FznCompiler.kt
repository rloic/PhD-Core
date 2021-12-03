package com.github.phd.core.mzn

interface Mzn2FznCompiler {

    fun compile(mznModel: MznModel, data: Map<String, Any>, solver: SolverKind, vararg mznArgs: String): FznModel

}