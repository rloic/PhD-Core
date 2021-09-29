package com.github.rloic.phd.core.cryptography.attacks.boomerang

import com.github.rloic.phd.core.arrays.IntMatrix
import com.github.rloic.phd.core.cryptography.attacks.boomerang.util.Bounds
import com.github.rloic.phd.core.cryptography.attacks.differential.Sbox
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.ints.IntSet
import it.unimi.dsi.fastutil.objects.Object2IntMap
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectSet
import javaextensions.lang.double
import org.chocosolver.solver.constraints.extension.Tuples
import kotlin.math.log2

class SPNSboxTables(
    val S: Sbox,
    val probaFactor: Int = 1
) {

    data class UBCTArgs(val γ: Int, val θ: Int, val δ: Int)
    data class LBCTArgs(val γ: Int, val λ: Int, val δ: Int)
    data class EBCTArgs(val γ: Int, val θ: Int, val λ: Int, val δ: Int)

    class BCTTables(
        val bct: IntMatrix,
        val ubct: Object2IntMap<UBCTArgs>,
        val lbct: Object2IntMap<LBCTArgs>,
        val ebct: Object2IntMap<EBCTArgs>
    ) {
        operator fun component1() = bct
        operator fun component2() = ubct
        operator fun component3() = lbct
        operator fun component4() = ebct
    }

    private val DDT: IntMatrix
    private val BCT: IntMatrix

    private val UBCT: Object2IntMap<UBCTArgs>
    private val LBCT: Object2IntMap<LBCTArgs>
    private val EBCT: Object2IntMap<EBCTArgs>

    private val DDToutDiffs: Array<IntSet>
    private val DDTinDiffs: Array<IntSet>
    private val BCToutDiffs: Array<IntSet>
    private val BCTinDiffs: Array<IntSet>
    private val probaExponents: IntArray

    val relationDDT: Tuples
    val relationDDTBounds: Bounds

    val relationDDT2: Tuples
    val relationDDT2Bounds: Bounds

    val relationBCT: Tuples
    val relationBCTBounds: Bounds

    val relationUBCT: Tuples
    val relationUBCTBounds: Bounds

    val relationLBCT: Tuples
    val relationLBCTBounds: Bounds

    val relationEBCT: Tuples
    val relationEBCTBounds: Bounds

    val min = S.values.minOrNull()!!
    val max = S.values.maxOrNull()!!
    val values get() = S.values.toSet()

    init {
        DDT = generateDDT()
        val (bct, ubct, lbct, ebct) = generateBCTs()
        BCT = bct
        UBCT = ubct
        LBCT = lbct
        EBCT = ebct

        DDToutDiffs = Array(S.size) { IntOpenHashSet() }
        DDTinDiffs = Array(S.size) { IntOpenHashSet() }
        BCToutDiffs = Array(S.size) { IntOpenHashSet() }
        BCTinDiffs = Array(S.size) { IntOpenHashSet() }

        for (input in S.values) {
            for (output in S.values) {
                if (DDT[input, output] != 0) {
                    DDTinDiffs[output].add(input)
                    DDToutDiffs[input].add(output)
                }

                if (BCT[input, output] != 0) {
                    BCTinDiffs[output].add(input)
                    BCToutDiffs[input].add(output)
                }
            }
        }

        probaExponents = IntArray(sboxSize + 1) { i ->
            Math.round(-probaFactor * log2(i.toDouble() / sboxSize)).toInt()
        }

        relationDDT = createRelationDDT(1)
        relationDDTBounds = probabilityBounds(relationDDT)
        relationDDT2 = createRelationDDT(2)
        relationDDT2Bounds = probabilityBounds(relationDDT2)
        relationBCT = createRelationBCT()
        relationBCTBounds = probabilityBounds(relationBCT)
        relationUBCT = createRelationUBCT()
        relationUBCTBounds = probabilityBounds(relationUBCT)
        relationLBCT = createRelationLBCT()
        relationLBCTBounds = probabilityBounds(relationLBCT)
        relationEBCT = createRelationEBCT()
        relationEBCTBounds = probabilityBounds(relationEBCT)
    }

    val sboxSize get() = S.size

    fun ddt(α: Int, β: Int) = DDT[α, β]
    fun bct(γ: Int, δ: Int) = BCT[γ, δ]

    fun ddtProba(α: Int, β: Int) = proba(DDT[α, β])
    fun bctProba(γ: Int, δ: Int) = proba(BCT[γ, δ])

    fun ubctProba(γ: Int, θ: Int, δ: Int) = proba(UBCT.getInt(UBCTArgs(γ, θ, δ)))
    fun lbctProba(γ: Int, λ: Int, δ: Int) = proba(LBCT.getInt(LBCTArgs(γ, λ, δ)))
    fun ebctProba(γ: Int, θ: Int, λ: Int, δ: Int) = proba(EBCT.getInt(EBCTArgs(γ, θ, λ, δ)))

    fun getPossibleInputDiffsDDT(output: Int) = DDTinDiffs[output]
    fun getPossibleOutputDiffsDDT(input: Int) = DDToutDiffs[input]
    fun getPossibleInputDiffsBCT(output: Int) = BCTinDiffs[output]
    fun getPossibleOutputDiffsBCT(input: Int) = BCToutDiffs[input]

    fun getUBCTEntrySet(): ObjectSet<Entry<UBCTArgs>> = UBCT.object2IntEntrySet()
    fun getLBCTEntrySet(): ObjectSet<Entry<LBCTArgs>> = LBCT.object2IntEntrySet()
    fun getEBCTEntrySet(): ObjectSet<Entry<EBCTArgs>> = EBCT.object2IntEntrySet()

    private fun generateDDT(): IntMatrix {
        val ddt = IntMatrix(S.size, S.size)
        for (x in 0 until S.size) {
            for (α in 0 until S.size) {
                val β = S(x) xor S(x xor α)
                ddt[α, β] += 1
            }
        }
        return ddt
    }

    private fun generateBCTs(): BCTTables {
        val bct = IntMatrix(S.size, S.size)
        val ubct = Object2IntOpenHashMap<UBCTArgs>()
        val lbct = Object2IntOpenHashMap<LBCTArgs>()
        val ebct = Object2IntOpenHashMap<EBCTArgs>()

        for (x in S.values) {
            for (γ in S.values) {
                for (δ in S.values) {
                    if (S.inv(S(x) xor δ) xor S.inv(S(x xor γ) xor δ) == γ) {
                        bct[γ, δ] += 1
                        val θ = S(x) xor S(x xor γ)
                        val λ = x xor S.inv(S(x) xor δ)
                        ubct.increaseByOne(UBCTArgs(γ, θ, δ))
                        lbct.increaseByOne(LBCTArgs(γ, λ, δ))
                        ebct.increaseByOne(EBCTArgs(γ, θ, λ, δ))
                    }
                }
            }
        }

        return BCTTables(bct, ubct, lbct, ebct)
    }

    private fun <K> Object2IntMap<K>.increaseByOne(key: K) {
        val current = if (containsKey(key)) getInt(key) else 0
        put(key, current + 1)
    }

    private fun proba(count: Int) = double(count) / double(S.size)

    fun createRelationDDT(factor: Int): Tuples {
        val tuples = Tuples(true)

        for (γ in 1 until sboxSize) {
            for (δ in 1 until sboxSize) {
                if (ddt(γ, δ) != 0) {
                    tuples.add(γ, δ, factor * probaExponents[ddt(γ, δ)])
                }
            }
        }

        return tuples
    }

    fun createRelationBCT(): Tuples {
        val tuples = Tuples(true)

        for (γ in 1 until sboxSize) {
            for (δ in 1 until sboxSize) {
                if (bct(γ, δ) != 0) {
                    tuples.add(γ, δ, probaExponents[bct(γ, δ)])
                }
            }
        }

        return tuples
    }

    fun createRelationUBCT(): Tuples {
        val tuples = Tuples(true)

        getUBCTEntrySet().stream()
            .filter { (key, _) -> key.γ != 0 && key.δ != 0 && key.θ != 0 }
            .forEach { entry -> addKeyValueInTable(tuples, entry.key, entry.intValue) }

        return tuples
    }

    fun createRelationLBCT(): Tuples {
        val tuples = Tuples(true)

        getLBCTEntrySet().stream()
            .filter { (key, _) -> key.γ != 0 && key.δ != 0 && key.λ != 0 }
            .forEach { entry -> addKeyValueInTable(tuples, entry.key, entry.intValue) }

        return tuples
    }

    fun createRelationEBCT(): Tuples {
        val tuples = Tuples(true)

        getEBCTEntrySet().stream()
            .filter { (key, _) -> key.γ != 0 && key.δ != 0 && key.θ != 0 && key.λ != 0 }
            .forEach { entry -> addKeyValueInTable(tuples, entry.key, entry.intValue) }

        return tuples
    }

    private fun addKeyValueInTable(table: Tuples, key: UBCTArgs, value: Int) {
        table.add(key.γ, key.θ, key.δ, probaExponents[value])
    }

    private fun addKeyValueInTable(table: Tuples, key: LBCTArgs, value: Int) {
        table.add(key.γ, key.λ, key.δ, probaExponents[value])
    }

    private fun addKeyValueInTable(table: Tuples, key: EBCTArgs, value: Int) {
        table.add(key.γ, key.θ, key.λ, key.δ, probaExponents[value])
    }

    private fun probabilityBounds(table: Tuples): Bounds {
        var min = Int.MAX_VALUE
        var max = Int.MIN_VALUE

        for (i in 0 until table.nbTuples()) {
            val tuple = table[i]
            val probability = tuple[tuple.size - 1]
            if (probability < min) min = probability
            if (probability > max) max = probability
        }

        return Bounds(min, max)
    }

}