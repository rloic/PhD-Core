package com.github.rloic.phd.core.cryptography.rijndael

import com.github.rloic.phd.core.cryptography.SboxTuples
import javaextensions.io.getResourceAsFile
import javaextensions.util.intArrayFrom
import javaextensions.util.reshape
import org.chocosolver.solver.constraints.extension.Tuples
import java.io.File

object Rijndael {

   val SEQ_SIZE = 8

   val VALUE_RANGE = 0..255

   val NB_ROWS = 4

   val maxRounds = intArrayOf(
      10, 11, 12, 13, 14,
      11, 11, 12, 13, 14,
      12, 12, 12, 13, 14,
      13, 13, 13, 13, 14,
      14, 14, 14, 14, 14,
   ).reshape(5, 5)

   val shifts = intArrayOf(
      0, 1, 2, 3,
      0, 1, 2, 3,
      0, 1, 2, 3,
      0, 1, 2, 4,
      0, 1, 3, 4
   ).reshape(5, 4)

   val SBOX = intArrayFrom(getResourceAsFile("sboxes/rijndael/sb.txt"))
   val SBOX_INV = intArrayFrom(getResourceAsFile("sboxes/rijndael/sb_inv.txt"))

   val OPTIMAL_SBOX_TRANSITION = intArrayFrom(getResourceAsFile("sboxes/rijndael/best_sb_transition.txt"))
   val OPTIMAL_INV_SBOX_TRANSITION = intArrayFrom(getResourceAsFile("sboxes/rijndael/best_sb_inv_transition.txt"))

   val SBOX_TUPLES = SboxTuples.generate(SBOX)

   val mul2: Tuples = buildTimes(2)
   val mul3: Tuples = buildTimes(3)
   val mul9: Tuples = buildTimes(9)
   val mul11: Tuples = buildTimes(11)
   val mul13: Tuples = buildTimes(13)
   val mul14: Tuples = buildTimes(14)

   val XOR_TUPLES = generateXorTuples()

   private fun generateXorTuples(): Tuples {
      val tuples = Tuples()
      for (i in VALUE_RANGE) {
         for (j in VALUE_RANGE) {
            tuples.add(i, j, i xor j)
         }
      }
      return tuples
   }

   private fun buildTimes(n: Int): Tuples {
      val tuples = Tuples(true)
      for (i in VALUE_RANGE) {
         tuples.add(i, galoisFieldMul(n, i))
      }
      return tuples
   }

   private fun galoisFieldMul(a: Int, b: Int): Int {
      var p = 0; var a = a; var b= b
      for (counter in 0 until SEQ_SIZE) {
         if ((b and 1) != 0) {
            p = p xor a
         }
         val highBitSet = (a and 0x80 != 0)
         a = a shl 1
         if (highBitSet) a = a xor 0x11B
         b = b shr 1
      }
      return p
   }
}