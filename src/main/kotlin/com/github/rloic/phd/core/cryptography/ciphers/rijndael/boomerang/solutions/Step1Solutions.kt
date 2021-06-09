package com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.solutions

import com.github.rloic.phd.core.arrays.IntTensor3
import com.github.rloic.phd.core.arrays.Matrix
import com.github.rloic.phd.core.arrays.Tensor3
import com.github.rloic.phd.core.cryptography.attacks.ConfiguredBy
import com.github.rloic.phd.core.cryptography.attacks.Step1Solution
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.RkRijndael
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.SkRijndael


data class TrailVar(val upper: Int, val lower: Int)
open class BoomerangVar(val Δ: TrailVar, val free: TrailVar)
class BoomerangSbVar(Δ: TrailVar, free: TrailVar, val freeS: TrailVar): BoomerangVar(Δ, free)
class OptionalBoomerangSbVar(Δ: TrailVar, free: TrailVar, val freeS: TrailVar?): BoomerangVar(Δ, free)


@Suppress("NonAsciiCharacters")
fun RijndaelBoomerangCipher(
    X: Tensor3<BoomerangSbVar>,
    Y: Tensor3<BoomerangVar>,
    Z: Tensor3<BoomerangVar>,
) = object : RijndaelBoomerangCipher {
    override val X get() = X
    override val Y get() = Y
    override val Z get() = Z
}

@Suppress("NonAsciiCharacters")
interface RijndaelBoomerangCipher {
    val X: Tensor3<BoomerangSbVar>
    val Y: Tensor3<BoomerangVar>
    val Z: Tensor3<BoomerangVar>
}

@Suppress("NonAsciiCharacters")
interface RijndaelBoomerangCipherWithKeySchedule : RijndaelBoomerangCipher {
    val WK: Matrix<OptionalBoomerangSbVar>
}

@Suppress("NonAsciiCharacters")
class EnumerateRijndaelBoomerangSkStep1Solution(
    override val config: SkRijndael,
    override val X: Tensor3<BoomerangSbVar>,
    override val Y: Tensor3<BoomerangVar>,
    override val Z: Tensor3<BoomerangVar>,
) : ConfiguredBy<SkRijndael>, RijndaelBoomerangCipher {

}

@Suppress("NonAsciiCharacters")
class OptimizeRijndaelBoomerangSkStep1Solution(
    override val config: SkRijndael,
    override val objStep1: Int,
    override val X: Tensor3<BoomerangSbVar>,
    override val Y: Tensor3<BoomerangVar>,
    override val Z: Tensor3<BoomerangVar>,
) : ConfiguredBy<SkRijndael>, Step1Solution, RijndaelBoomerangCipher {

}

@Suppress("NonAsciiCharacters")
open class EnumerateRijndaelBoomerangRkStep1Solution(
    override val config: RkRijndael,
    override val X: Tensor3<BoomerangSbVar>,
    override val Y: Tensor3<BoomerangVar>,
    override val Z: Tensor3<BoomerangVar>,
    override val WK: Matrix<OptionalBoomerangSbVar>
) : ConfiguredBy<RkRijndael>, RijndaelBoomerangCipherWithKeySchedule {

}

@Suppress("NonAsciiCharacters")
class OptimizeRijndaelBoomerangRkStep1Solution(
    override val config: RkRijndael,
    override val objStep1: Int,
    override val X: Tensor3<BoomerangSbVar>,
    override val Y: Tensor3<BoomerangVar>,
    override val Z: Tensor3<BoomerangVar>,
    override val WK: Matrix<OptionalBoomerangSbVar>
) : ConfiguredBy<RkRijndael>, Step1Solution, RijndaelBoomerangCipherWithKeySchedule {

}