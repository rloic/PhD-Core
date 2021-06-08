package com.github.rloic.phd.core.cryptography.boomerang

object BoomerangRules {

    fun isDDT(
        DXupper: Boolean, freeXupper: Boolean, freeSBupper: Boolean,
        DXlower: Boolean, freeXlower: Boolean, freeSBlower: Boolean,
    ) =
        (!(DXupper) && DXlower && !(freeXlower) && !(freeSBlower)) || (DXupper && !(freeXupper) && !(freeSBupper) && !(DXlower))

    fun isBCT(
        DXupper: Boolean, freeXupper: Boolean, freeSBupper: Boolean,
        DXlower: Boolean, freeXlower: Boolean, freeSBlower: Boolean,
    ) = (DXupper && !(freeXupper) && freeSBupper && DXlower && freeXlower && !(freeSBlower))

    fun isDDT2(
        DXupper: Boolean, freeXupper: Boolean, freeSBupper: Boolean,
        DXlower: Boolean, freeXlower: Boolean, freeSBlower: Boolean,
    ) =
        (DXupper && !(freeXupper) && !(freeSBupper) && DXlower && freeXlower && freeSBlower) || (DXupper && freeXupper && freeSBupper && DXlower && !(freeXlower) && !(freeSBlower))

    fun isUBCT(
        DXupper: Boolean, freeXupper: Boolean, freeSBupper: Boolean,
        DXlower: Boolean, freeXlower: Boolean, freeSBlower: Boolean,
    ) = (DXupper && !(freeXupper) && !(freeSBupper) && DXlower && freeXlower && !(freeSBlower))

    fun isLBCT(
        DXupper: Boolean, freeXupper: Boolean, freeSBupper: Boolean,
        DXlower: Boolean, freeXlower: Boolean, freeSBlower: Boolean,
    ) = (DXupper && !(freeXupper) && freeSBupper && DXlower && !(freeXlower) && !(freeSBlower))

    fun isEBCT(
        DXupper: Boolean, freeXupper: Boolean, freeSBupper: Boolean,
        DXlower: Boolean, freeXlower: Boolean, freeSBlower: Boolean,
    ) = (DXupper && !(freeXupper) && !(freeSBupper) && DXlower && !(freeXlower) && !(freeSBlower))

    fun isDDT(DXupper: Int, freeXUpper: Int, freeSBupper: Int, DXlower: Int, freeXlower: Int, freeSBlower: Int) =
        isDDT(DXupper == 1, freeXUpper == 1, freeSBupper == 1, DXlower == 1, freeXlower == 1, freeSBlower == 1)

    fun isDDT(DXupper: Int?, freeXUpper: Int?, freeSBupper: Int?, DXlower: Int?, freeXlower: Int?, freeSBlower: Int?) =
        isDDT(DXupper == 1, freeXUpper == 1, freeSBupper == 1, DXlower == 1, freeXlower == 1, freeSBlower == 1)

    fun isDDT2(DXupper: Int, freeXUpper: Int, freeSBupper: Int, DXlower: Int, freeXlower: Int, freeSBlower: Int) =
        isDDT2(DXupper == 1, freeXUpper == 1, freeSBupper == 1, DXlower == 1, freeXlower == 1, freeSBlower == 1)

    fun isDDT2(DXupper: Int?, freeXUpper: Int?, freeSBupper: Int?, DXlower: Int?, freeXlower: Int?, freeSBlower: Int?) =
        isDDT2(DXupper == 1, freeXUpper == 1, freeSBupper == 1, DXlower == 1, freeXlower == 1, freeSBlower == 1)

    fun isBCT(DXupper: Int, freeXUpper: Int, freeSBupper: Int, DXlower: Int, freeXlower: Int, freeSBlower: Int) =
        isBCT(DXupper == 1, freeXUpper == 1, freeSBupper == 1, DXlower == 1, freeXlower == 1, freeSBlower == 1)

    fun isBCT(DXupper: Int?, freeXUpper: Int?, freeSBupper: Int?, DXlower: Int?, freeXlower: Int?, freeSBlower: Int?) =
        isBCT(DXupper == 1, freeXUpper == 1, freeSBupper == 1, DXlower == 1, freeXlower == 1, freeSBlower == 1)

    fun isUBCT(DXupper: Int, freeXUpper: Int, freeSBupper: Int, DXlower: Int, freeXlower: Int, freeSBlower: Int) =
        isUBCT(DXupper == 1, freeXUpper == 1, freeSBupper == 1, DXlower == 1, freeXlower == 1, freeSBlower == 1)

    fun isUBCT(DXupper: Int?, freeXUpper: Int?, freeSBupper: Int?, DXlower: Int?, freeXlower: Int?, freeSBlower: Int?) =
        isUBCT(DXupper == 1, freeXUpper == 1, freeSBupper == 1, DXlower == 1, freeXlower == 1, freeSBlower == 1)

    fun isLBCT(DXupper: Int, freeXUpper: Int, freeSBupper: Int, DXlower: Int, freeXlower: Int, freeSBlower: Int) =
        isLBCT(DXupper == 1, freeXUpper == 1, freeSBupper == 1, DXlower == 1, freeXlower == 1, freeSBlower == 1)

    fun isLBCT(DXupper: Int?, freeXUpper: Int?, freeSBupper: Int?, DXlower: Int?, freeXlower: Int?, freeSBlower: Int?) =
        isLBCT(DXupper == 1, freeXUpper == 1, freeSBupper == 1, DXlower == 1, freeXlower == 1, freeSBlower == 1)

    fun isEBCT(DXupper: Int, freeXUpper: Int, freeSBupper: Int, DXlower: Int, freeXlower: Int, freeSBlower: Int) =
        isEBCT(DXupper == 1, freeXUpper == 1, freeSBupper == 1, DXlower == 1, freeXlower == 1, freeSBlower == 1)

    fun isEBCT(DXupper: Int?, freeXUpper: Int?, freeSBupper: Int?, DXlower: Int?, freeXlower: Int?, freeSBlower: Int?) =
        isEBCT(DXupper == 1, freeXUpper == 1, freeSBupper == 1, DXlower == 1, freeXlower == 1, freeSBlower == 1)

}