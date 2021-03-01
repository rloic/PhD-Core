package com.github.rloic.phd.core.mzn

import com.github.rloic.phd.core.mzn.utils.ForbidPreviousCompleteSolutionIterator
import com.github.rloic.phd.core.mzn.utils.ForbidPreviousPartialSolutionIterator

interface MznSolver {

    fun optimize(model: MznModel.Optimization, data: Map<String, Any>): MznSolution?
    fun solveOnce(model: MznModel.PartialSearch, data: Map<String, Any>): MznSolution?
    fun solveOnce(model: MznModel.CompleteSearch, data: Map<String, Any>): MznSolution?
    fun enumerate(model: MznModel.PartialSearch, data: Map<String, Any>): Iterator<MznSolution> = makeIterator(model, data)
    fun enumerate(model: MznModel.CompleteSearch, data: Map<String, Any>): Iterator<MznSolution> = makeIterator(model, data)

    fun makeIterator(model: MznModel.CompleteSearch, data: Map<String, Any>): Iterator<MznSolution> =
        ForbidPreviousCompleteSolutionIterator(this, model, data)

    fun makeIterator(model: MznModel.PartialSearch, data: Map<String, Any>): Iterator<MznSolution> =
        ForbidPreviousPartialSolutionIterator(this, model, data)

}