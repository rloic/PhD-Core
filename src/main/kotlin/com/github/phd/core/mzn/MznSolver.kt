package com.github.phd.core.mzn

import com.github.phd.core.mzn.utils.ForbidPreviousCompleteSolutionIterator
import com.github.phd.core.mzn.utils.ForbidPreviousPartialSolutionIterator

interface MznSolver {

    fun optimize(model: MznModel.Optimization, data: Map<String, Any>, vararg mznArgs: String): MznSolution?
    fun solveOnce(model: MznModel.PartialSearch, data: Map<String, Any>, vararg mznArgs: String): MznSolution?
    fun solveOnce(model: MznModel.CompleteSearch, data: Map<String, Any>, vararg mznArgs: String): MznSolution?
    fun enumerate(model: MznModel.PartialSearch, data: Map<String, Any>, vararg mznArgs: String): Iterator<MznSolution> = makeIterator(model, data)
    fun enumerate(model: MznModel.CompleteSearch, data: Map<String, Any>, vararg mznArgs: String): Iterator<MznSolution> = makeIterator(model, data)

    fun makeIterator(model: MznModel.CompleteSearch, data: Map<String, Any>, vararg mznArgs: String): Iterator<MznSolution> =
        ForbidPreviousCompleteSolutionIterator(this, model, data, mznArgs)

    fun makeIterator(model: MznModel.PartialSearch, data: Map<String, Any>, vararg mznArgs: String): Iterator<MznSolution> =
        ForbidPreviousPartialSolutionIterator(this, model, data, mznArgs)

}