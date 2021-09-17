package com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.solutions

import com.github.rloic.phd.core.arrays.Matrix
import com.github.rloic.phd.core.arrays.Tensor3
import com.github.rloic.phd.core.cryptography.attacks.ConfiguredBy
import com.github.rloic.phd.core.cryptography.attacks.Step1Solution
import com.github.rloic.phd.core.cryptography.attacks.boomerang.BoomerangRules
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.RkRijndael
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.SkRijndael


data class TrailVar(val upper: Int, val lower: Int)
data class BoomerangLinVar(val Δ: TrailVar, val free: TrailVar)
data class BoomerangSbVar(val Δ: TrailVar, val free: TrailVar, val freeS: TrailVar)
data class BoomerangOptionalSbVar(val Δ: TrailVar, val free: TrailVar, val freeS: TrailVar?)

@Suppress("NonAsciiCharacters")
interface RijndaelBoomerangCipher {
    val X: Tensor3<BoomerangSbVar>
    val Y: Tensor3<BoomerangLinVar>
    val Z: Tensor3<BoomerangLinVar>
    val ATTACK_I: Boolean
    val ATTACK_II: Boolean
    val ATTACK_III: Boolean
    val p2q2r: Int
    val rb: Int
    val rf: Int
}

fun <T> T.table(i: Int) where T: ConfiguredBy<out SkRijndael>, T: RijndaelBoomerangCipher =
    Matrix(4, config.Nb) { j, k -> BoomerangRules.table(X[i, j, k]) }

@Suppress("NonAsciiCharacters")
interface RijndaelBoomerangCipherWithKeySchedule : RijndaelBoomerangCipher {
    val WK: Matrix<BoomerangOptionalSbVar>
}

fun <T> T.subKey(i: Int) where T: ConfiguredBy<RkRijndael>, T: RijndaelBoomerangCipherWithKeySchedule =
    Matrix(4, config.Nb) { j, k -> WK[j, i * config.Nb + k] }

fun <T> T.subKeyTable(i: Int) where T: ConfiguredBy<RkRijndael>, T: RijndaelBoomerangCipherWithKeySchedule =
    Matrix(4, config.Nb) { j, k -> BoomerangRules.table(WK[j, i * config.Nb + k]) }

@Suppress("NonAsciiCharacters")
class EnumerateRijndaelBoomerangSkStep1Solution(
    override val config: SkRijndael,
    override val X: Tensor3<BoomerangSbVar>,
    override val Y: Tensor3<BoomerangLinVar>,
    override val Z: Tensor3<BoomerangLinVar>,
    override val ATTACK_I: Boolean,
    override val ATTACK_II: Boolean,
    override val ATTACK_III: Boolean,
    override val p2q2r: Int,
    override val rb: Int,
    override val rf: Int,
) : ConfiguredBy<SkRijndael>, RijndaelBoomerangCipher

@Suppress("NonAsciiCharacters")
class OptimizeRijndaelBoomerangSkStep1Solution(
    override val config: SkRijndael,
    override val objStep1: Int,
    override val X: Tensor3<BoomerangSbVar>,
    override val Y: Tensor3<BoomerangLinVar>,
    override val Z: Tensor3<BoomerangLinVar>,
    override val ATTACK_I: Boolean,
    override val ATTACK_II: Boolean,
    override val ATTACK_III: Boolean,
    override val p2q2r: Int,
    override val rb: Int,
    override val rf: Int,
) : ConfiguredBy<SkRijndael>, Step1Solution, RijndaelBoomerangCipher

@Suppress("NonAsciiCharacters")
open class EnumerateRijndaelBoomerangRkStep1Solution(
    override val config: RkRijndael,
    override val X: Tensor3<BoomerangSbVar>,
    override val Y: Tensor3<BoomerangLinVar>,
    override val Z: Tensor3<BoomerangLinVar>,
    override val WK: Matrix<BoomerangOptionalSbVar>,
    override val ATTACK_I: Boolean,
    override val ATTACK_II: Boolean,
    override val ATTACK_III: Boolean,
    override val p2q2r: Int,
    override val rb: Int,
    override val rf: Int,
) : ConfiguredBy<RkRijndael>, RijndaelBoomerangCipherWithKeySchedule



@Suppress("NonAsciiCharacters")
class OptimizeRijndaelBoomerangRkStep1Solution(
    override val config: RkRijndael,
    override val objStep1: Int,
    override val X: Tensor3<BoomerangSbVar>,
    override val Y: Tensor3<BoomerangLinVar>,
    override val Z: Tensor3<BoomerangLinVar>,
    override val WK: Matrix<BoomerangOptionalSbVar>,
    override val ATTACK_I: Boolean,
    override val ATTACK_II: Boolean,
    override val ATTACK_III: Boolean,
    override val p2q2r: Int,
    override val rb: Int,
    override val rf: Int,
) : ConfiguredBy<RkRijndael>, Step1Solution, RijndaelBoomerangCipherWithKeySchedule