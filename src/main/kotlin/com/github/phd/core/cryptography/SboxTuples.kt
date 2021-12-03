package com.github.phd.core.cryptography

import com.github.phd.core.arrays.IntMatrix
import com.github.phd.core.error.panic
import org.chocosolver.solver.constraints.extension.Tuples

object SboxTuples {

   // Todo: adapt for skinny
   fun generate(sbox: IntArray, factor: Int = 1): Tuples {
      val tuples = Tuples()

      val transitions = List(sbox.size) { mutableSetOf<Int>() }
      val probas = IntMatrix(sbox.size, sbox.size)

      for (i in 0 until sbox.size) {
         for (j in 0 until sbox.size) {
            val transition = sbox[j] xor sbox[j xor i]
            probas[i, transition] += 1
            if (probas[i, transition] == 1) {
               transitions[i] += sbox[j] xor sbox[i xor j]
            }
         }
      }

      tuples.add(0, 0, 0)
      for (i in 1 until sbox.size) {
         for (transition in transitions[i]) {
            val p = probas[i, transition] * factor / 2
            tuples.add(i, transition, log2(sbox.size) * factor - p)
         }
      }

      return tuples

   }

   private fun log2(n: Int): Int {
      var i = 1
      var pow = 0
      while (i < n) {
         i = i shl 1
         pow += 1
      }
      if (i != n) panic("$n is not a power of two.")
      return pow
   }

}