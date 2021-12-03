package com.github.phd.core.files.latex

import com.github.phd.core.utils.Presenter
import java.io.File
import java.lang.Appendable
import java.lang.StringBuilder
import java.util.*

object Latex {

    fun <T> write(fn: (Appendable) -> Presenter<T>, data: T, path: String) {
        val rawContent = StringBuilder()
        val presenter = fn(rawContent)
        presenter.present(data)
        write(LatexDocument(LatexContent(rawContent.toString())), path)
        presenter.close()
    }

    fun <T> preview(fn: (Appendable) -> Presenter<T>, data: T) {
        val rawContent = StringBuilder()
        val presenter = fn(rawContent)
        presenter.present(data)
        preview(LatexDocument(LatexContent(rawContent.toString())))
        presenter.close()
    }

    fun write(latexDocument: LatexDocument, path: String? = null) {
        val texFile = if (path != null) File(path) else File.createTempFile("test", ".tex")
        texFile.writeText(latexDocument.content.rawContent)
    }

    fun preview(latexDocument: LatexDocument) {
        val texFile = File.createTempFile("test", ".tex")
        texFile.writeText(latexDocument.content.rawContent)

        ProcessBuilder("tectonic", texFile.absolutePath)
            // .inheritIO()
            .start()
            .waitFor()

        val pdfFile = File(texFile.absolutePath.substringBeforeLast(".") + ".pdf")
        ProcessBuilder("evince", pdfFile.absolutePath)
            .inheritIO()
            .start()
            .waitFor()

        texFile.delete()
        pdfFile.delete()
    }
}