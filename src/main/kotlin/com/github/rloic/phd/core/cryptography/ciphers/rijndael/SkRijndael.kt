package com.github.rloic.phd.core.cryptography.ciphers.rijndael

import com.github.rloic.phd.core.utils.FromArgs
import com.github.rloic.phd.core.utils.expectArgument

open class SkRijndael(Nr: Int, textSize: TextSize): Rijndael(Nr, textSize) {

   companion object : FromArgs<SkRijndael> {
      override fun from(args: Map<String, String>) = SkRijndael(
         args.expectArgument("Nr").toInt(),
         TextSize(args.expectArgument("TextSize").toInt())
      )
   }

   override fun toString() = "SkRijndael(Nr=$Nr, textSize=$textSize)"
   @Override
   fun copy(Nr: Int = this.Nr, textSize: TextSize = this.textSize) = SkRijndael(Nr, textSize)

   fun withKey(keySize: KeySize) = RkRijndael(Nr, textSize, keySize)
}