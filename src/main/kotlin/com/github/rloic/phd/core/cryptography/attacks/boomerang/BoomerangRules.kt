package com.github.rloic.phd.core.cryptography.attacks.boomerang

import com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.solutions.BoomerangSbVar
import com.github.rloic.phd.core.cryptography.ciphers.rijndael.boomerang.solutions.BoomerangOptionalSbVar

object BoomerangRules {

    fun table(variable: BoomerangSbVar): BoomerangTable {
        return table(
            variable.Δ.upper, variable.free.upper, variable.freeS.upper,
            variable.Δ.lower, variable.free.lower, variable.freeS.lower,
        )
    }

    fun table(variable: BoomerangOptionalSbVar): BoomerangTable {
        return table(
            variable.Δ.upper, variable.free.upper, variable.freeS?.upper,
            variable.Δ.lower, variable.free.lower, variable.freeS?.lower,
        )
    }

    fun table(
        DXupper: Int?, freeXupper: Int?, freeSBupper: Int?,
        DXlower: Int?, freeXlower: Int?, freeSBlower: Int?,
    ): BoomerangTable {
        return when {
            isDDT(DXupper, freeXupper, freeSBupper, DXlower, freeXlower, freeSBlower) -> BoomerangTable.DDT
            isDDT2(DXupper, freeXupper, freeSBupper, DXlower, freeXlower, freeSBlower) -> BoomerangTable.DDT2
            isBCT(DXupper, freeXupper, freeSBupper, DXlower, freeXlower, freeSBlower) -> BoomerangTable.BCT
            isUBCT(DXupper, freeXupper, freeSBupper, DXlower, freeXlower, freeSBlower) -> BoomerangTable.UBCT
            isLBCT(DXupper, freeXupper, freeSBupper, DXlower, freeXlower, freeSBlower) -> BoomerangTable.LBCT
            isEBCT(DXupper, freeXupper, freeSBupper, DXlower, freeXlower, freeSBlower) -> BoomerangTable.EBCT
            else -> BoomerangTable.None
        }
    }

    fun isDDT(
        DXupper: Int?, freeXupper: Int?, freeSBupper: Int?,
        DXlower: Int?, freeXlower: Int?, freeSBlower: Int?,
    ) = isDDTupper(DXupper, freeXupper, freeSBupper, DXlower, freeXlower, freeSBlower) ||
            isDDTLower(DXupper, freeXupper, freeSBupper, DXlower, freeXlower, freeSBlower)

    @Suppress("UNUSED_PARAMETER")
    fun isDDTupper(
        DXupper: Int?, freeXupper: Int?, freeSBupper: Int?,
        DXlower: Int?, freeXlower: Int?, freeSBlower: Int?,
    ) = (DXupper == 1 && (freeXupper == 0) && (freeSBupper == 0) && (DXlower == 0))

    @Suppress("UNUSED_PARAMETER")
    fun isDDTLower(
        DXupper: Int?, freeXupper: Int?, freeSBupper: Int?,
        DXlower: Int?, freeXlower: Int?, freeSBlower: Int?,
    ) = ((DXupper == 0) && DXlower == 1 && (freeXlower == 0) && (freeSBlower == 0))

    fun isBCT(
        DXupper: Int?, freeXupper: Int?, freeSBupper: Int?,
        DXlower: Int?, freeXlower: Int?, freeSBlower: Int?,
    ) = (DXupper == 1 && (freeXupper == 0) && freeSBupper == 1 && DXlower == 1 && freeXlower == 1 && (freeSBlower == 0))

    fun isDDT2(
        DXupper: Int?, freeXupper: Int?, freeSBupper: Int?,
        DXlower: Int?, freeXlower: Int?, freeSBlower: Int?,
    ) = isDDT2Upper(DXupper, freeXupper, freeSBupper, DXlower, freeXlower, freeSBlower) ||
            isDDT2Lower(DXupper, freeXupper, freeSBupper, DXlower, freeXlower, freeSBlower)

    fun isDDT2Upper(
        DXupper: Int?, freeXupper: Int?, freeSBupper: Int?,
        DXlower: Int?, freeXlower: Int?, freeSBlower: Int?,
    ) = (DXupper == 1 && (freeXupper == 0) && (freeSBupper == 0) && DXlower == 1 && freeXlower == 1 && freeSBlower == 1)

    fun isDDT2Lower(
        DXupper: Int?, freeXupper: Int?, freeSBupper: Int?,
        DXlower: Int?, freeXlower: Int?, freeSBlower: Int?,
    ) = (DXupper == 1 && freeXupper == 1 && freeSBupper == 1 && DXlower == 1 && (freeXlower == 0) && (freeSBlower == 0))

    fun isUBCT(
        DXupper: Int?, freeXupper: Int?, freeSBupper: Int?,
        DXlower: Int?, freeXlower: Int?, freeSBlower: Int?,
    ) =
        (DXupper == 1) && (freeXupper == 0) && (freeSBupper == 0) && DXlower == 1 && freeXlower == 1 && (freeSBlower == 0)

    fun isLBCT(
        DXupper: Int?, freeXupper: Int?, freeSBupper: Int?,
        DXlower: Int?, freeXlower: Int?, freeSBlower: Int?,
    ) =
        (DXupper == 1) && (freeXupper == 0) && freeSBupper == 1 && DXlower == 1 && (freeXlower == 0) && (freeSBlower == 0)

    fun isEBCT(
        DXupper: Int?, freeXupper: Int?, freeSBupper: Int?,
        DXlower: Int?, freeXlower: Int?, freeSBlower: Int?,
    ) =
        (DXupper == 1) && (freeXupper == 0) && (freeSBupper == 0) && DXlower == 1 && (freeXlower == 0) && (freeSBlower == 0)


}