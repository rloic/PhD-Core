package com.github.rloic.phd.core.mzn.utils

import com.github.rloic.phd.core.mzn.MznModel
import com.github.rloic.phd.core.mzn.MznSolution
import com.github.rloic.phd.core.mzn.MznSolver

class ForbidPreviousCompleteSolutionIterator(
    private val solver: MznSolver,
    private val model: MznModel.CompleteSearch,
    private val data: Map<String, Any>
) : Iterator<MznSolution> {

    var unSat = false
    var solution: MznSolution? = null

    override fun hasNext(): Boolean {
        if (solution == null && !unSat) {
            val newSolution = solver.solveOnce(model, data)
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