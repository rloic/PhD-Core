package com.github.rloic.phd.core.mzn

import javaextensions.io.concat
import java.io.File

interface MznModelBuilder<Model: MznModel> {

    fun build(target: File): Model

    class Optimization(
        val modelParts: List<PartialMznModel>,
        val objectiveVar: MznVariable,
        val search: OptimizationSearch
    ) : MznModelBuilder<MznModel.Optimization> {

        override fun build(target: File): MznModel.Optimization {
            modelParts.compileInto(target)
            target.appendText("%%% Objective %%%\n")
            target.appendText("solve $search ${objectiveVar.name};\n")

            return MznModel.Optimization(target, objectiveVar, search)
        }
    }

    class CompleteAssignment(
        val modelParts: List<PartialMznModel>,
        val forbiddenSolution: File,
        val completeAssignment: (MznSolution) -> Assignment.Complete
    ) : MznModelBuilder<MznModel.CompleteSearch> {
        override fun build(target: File): MznModel.CompleteSearch {
            modelParts.compileInto(target)

            val forbiddenSolutionText = forbiddenSolution.readText()
            var solutionId = 0

            return MznModel.CompleteSearch(target, completeAssignment) { assignment ->
                target.appendText("\n%%% Forbid solution $solutionId %%%\n")
                target.appendText(assignment.content.replace("%SOL%", solutionId.toString()))
                target.appendText("\n")
                target.appendText(forbiddenSolutionText.replace("%SOL%", solutionId.toString()))
                target.appendText("\n")
                solutionId += 1
            }
        }
    }

    class PartialAssignment(
        val modelParts: List<PartialMznModel>,
        val decisionVars: PartialMznModel,
        val strategy: SearchStrategy,
        val valueSelector: ValueSelector,
        val forbiddenSolution: PartialMznModel,
        val partialAssignment: (MznSolution) -> Assignment.Partial
    ) : MznModelBuilder<MznModel.PartialSearch> {

        override fun build(target: File): MznModel.PartialSearch {
            modelParts.compileInto(target)
            target.appendText("%%% Configure search %%%\n")
            target.appendText("solve :: int_search(\n  ")
            target.concat(decisionVars.file)
            target.appendText("  , ${strategy.mzn}, ${valueSelector.mzn}, complete\n) satisfy;\n")

            val forbiddenSolutionText = forbiddenSolution.file.readText()
            var solutionId = 0

            return MznModel.PartialSearch(target, partialAssignment) { assignment ->
                target.appendText("\n%%% Forbid solution $solutionId %%%\n")
                target.appendText(assignment.content.replace("%SOL%", solutionId.toString()))
                target.appendText("\n")
                target.appendText(forbiddenSolutionText.replace("%SOL%", solutionId.toString()))
                target.appendText("\n")
                solutionId += 1
            }
        }
    }

}

internal fun List<PartialMznModel>.compileInto(target: File) {
    assert(target.extension == "mzn")

    if(target.exists()) {
        target.delete()
    }
    for (part in this) {
        target.appendText("%% Part: ${part.file.absolutePath}\n")
        target.concat(part.file)
        target.appendText("\n")
    }
}