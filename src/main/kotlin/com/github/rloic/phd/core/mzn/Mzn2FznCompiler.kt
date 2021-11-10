package com.github.rloic.phd.core.mzn

interface Mzn2FznCompiler {

    fun compile(mznModel: MznModel, data: Map<String, Any>, solver: SolverKind, vararg mznArgs: String): FznModel

}