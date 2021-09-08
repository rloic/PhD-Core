package com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.presenters

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.arrays.IntTensor3
import com.github.rloic.phd.core.arrays.Matrix
import com.github.rloic.phd.core.cryptography.attacks.ConfiguredBy
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.RkRijndael
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.solutions.Step2RijndaelBoomerangCipherWithKeySchedule
import com.github.rloic.phd.core.utils.Presenter
import java.io.File

class TextPresenter<T>(val folder: String) : Presenter<T>
where T : ConfiguredBy<RkRijndael>,
      T : Step2RijndaelBoomerangCipherWithKeySchedule {

    fun joinToText(m: IntTensor3): String {
        val builder = StringBuilder()
        var i = 0
        while (true) {
            var j = 0
            while (true) {
                var k = 0
                while (true) {
                    if (m[i, j, k] < 0) {
                        builder.append("NONE")
                    } else if (m[i, j, k] == 256) {
                        builder.append("FREE")
                    } else {
                        builder.append("0x" + Integer.toHexString(m[i, j, k]).padStart(2, '0'))
                    }

                    k += 1
                    if (k == m.dim3) break
                    builder.append(' ')
                }

                j += 1
                if (j == m.dim2) break
                builder.append('\n')
            }

            i += 1
            if(i == m.dim1) break
            builder.append("\n---\n")
        }
        return builder.toString()
    }

    private fun convertKey(Nr: Int, Nb: Int, key: IntMatrix) = IntTensor3(Nr + 1, 4, Nb) { i, j, k -> key[j, i * Nb + k] }
    private fun convertKey(Nr: Int, Nb: Int, key: Matrix<Int?>) = IntTensor3(Nr + 1, 4, Nb) { i, j, k -> key[j, i * Nb + k] ?: -1 }


    override fun present(data: T) {
        if (!File(folder).exists()) {
            File(folder).mkdirs()
        }

        File(folder, "xup.txt").writeText(joinToText(data.δXupper))
        File(folder, "xlo.txt").writeText(joinToText(data.δXlower))
        File(folder, "sxup.txt").writeText(joinToText(data.δSXupper))
        File(folder, "sxlo.txt").writeText(joinToText(data.δSXlower))
        File(folder, "kup.txt").writeText(joinToText(convertKey(data.config.Nr, data.config.Nb, data.δWKupper)))
        File(folder, "klo.txt").writeText(joinToText(convertKey(data.config.Nr, data.config.Nb, data.δWKlower)))
        File(folder, "skup.txt").writeText(joinToText(convertKey(data.config.Nr, data.config.Nb, data.δSWKupper)))
        File(folder, "sklo.txt").writeText(joinToText(convertKey(data.config.Nr, data.config.Nb, data.δSWKlower)))
        File(folder, "zup.txt").writeText(joinToText(data.δZupper))
        File(folder, "zlo.txt").writeText(joinToText(data.δZlower))
    }
}