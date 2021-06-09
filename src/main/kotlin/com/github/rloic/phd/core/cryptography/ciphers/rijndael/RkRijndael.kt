package com.github.rloic.phd.core.cryptography.ciphers.rijndael

import com.github.rloic.phd.core.utils.FromArgs
import com.github.rloic.phd.core.utils.expectArgument

open class RkRijndael(Nr: Int, textSize: TextSize, val keySize: KeySize): SkRijndael(Nr, textSize) {

   companion object : FromArgs<RkRijndael> {
      override fun from(args: Map<String, String>) = RkRijndael(
         args.expectArgument("Nr").toInt(),
         TextSize(args.expectArgument("TextSize").toInt()),
         KeySize(args.expectArgument("KeySize").toInt())
      )
   }

   val Nk: Int get() = keySize.nbBits / 32

   val MAX_NR: Int get() = MAX_ROUNDS[Nb - 4, Nk - 4]

   fun isSbColumn(ik: Int): Boolean {
      val nextIk = ik + 1
      return nextIk in (Nk until Nb * (Nr + 1)) && (nextIk % Nk == 0 || (Nk > 6 && nextIk % 4 == 0))
   }

   @Override
   fun copy(Nr: Int = this.Nr, textSize: TextSize = this.textSize, keySize: KeySize = this.keySize) = RkRijndael(Nr, textSize, keySize)

   override fun toString() = "RkRijndael(Nr=$Nr, Nb=$Nb, Nk=$Nk)"

}