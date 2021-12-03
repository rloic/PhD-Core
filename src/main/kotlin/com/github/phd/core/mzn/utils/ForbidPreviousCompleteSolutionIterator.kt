package com.github.phd.core.mzn.utils

import com.github.phd.core.mzn.MznModel
import com.github.phd.core.mzn.MznSolution
import com.github.phd.core.mzn.MznSolver

class ForbidPreviousCompleteSolutionIterator(
    private val solver: MznSolver,
    private val model: MznModel.CompleteSearch,
    private val data: Map<String, Any>,
    private val mznArgs: Array<out String>
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
        val previousCompleteAssignment = model.assignment(result)
        model.forbid(previousCompleteAssignment)
        solution = null
        return result
    }


}