package com.github.rloic.phd.core.cryptography.rijndael.boomerang.singlekey.step2.optimization

import com.github.rloic.phd.core.utils.Presenter
import java.io.File

class HTMLSolutionPresenter(private val file: File): Presenter<Solution> {

    override fun present(data: Solution) {

        file.writeText("""
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
  </head>
  <body>
""".trimIndent())
        file.writeText("""
  </body>
</html>
        """.trimIndent())

    }
}