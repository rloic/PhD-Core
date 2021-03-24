package javaextensions.io

import java.io.File
import java.io.IOException
import java.lang.RuntimeException
import java.io.FileOutputStream


fun mkdirs(folder: File): File? = try {
   folder.mkdirs()
   folder
} catch (ioe: IOException) {
   null
}

fun mkdirs(path: String): File? = mkdirs(File(path))

operator fun File.div(child: String) = File(this, child)
operator fun <E : Enum<E>> File.div(child: E) = File(this, child.toString())
operator fun File.div(child: Int) = File(this, child.toString())

fun File.concat(other: File) {

   other.bufferedReader().useLines { lines ->
      for (line in lines) {
         this.appendText(line)
         this.appendText("\n")
      }
   }

}

fun dir(path: String): File {
   val file = File(path)
   if (!file.exists()) {
      file.mkdirs()
   }
   if (!file.isDirectory) {
      throw RuntimeException("$file is not a directory")
   }
   return file
}

fun getResourceAsFile(resourcePath: String): File {
   val inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath)!!
   val tempFile = File.createTempFile(inputStream.hashCode().toString(), ".tmp")
   tempFile.deleteOnExit()
   FileOutputStream(tempFile).use { out ->
      //copy stream
      val buffer = ByteArray(1024)
      var bytesRead: Int
      while (inputStream.read(buffer).also { bytesRead = it } != -1) {
         out.write(buffer, 0, bytesRead)
      }
   }
   return tempFile

}