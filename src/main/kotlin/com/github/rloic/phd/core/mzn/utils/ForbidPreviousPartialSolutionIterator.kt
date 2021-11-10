package com.github.rloic.phd.core.mzn.utils

import com.github.rloic.phd.core.mzn.MznModel
import com.github.rloic.phd.core.mzn.MznSolution
import com.github.rloic.phd.core.mzn.MznSolver

class ForbidPreviousPartialSolutionIterator(
    private val solver: MznSolver,
    private val model: MznModel.PartialSearch,
    private val data: Map<String, Any>,
    private val mznArgs: Array<out String>,
) : Iterator<MznSolution> {

    var unSat = false
    var solution: MznSolution? = null

    override fun hasNext(): Boolean {
        if (solution == null && !unSat) {
            val newSolution = solver.solveOnce(model, data, *mznArgs)
            if (newSolution != null) {
                solution = newSolution
            } else {
                unSat = true
            }
        }
        return solution != null
    }

    override fun next(): MznSolution {
        val result = solution!!
        val previousPartialAssignment = model.assignment(result)
        model.forbid(previousPartialAssignment)
        solution = null
        return result
    }

}