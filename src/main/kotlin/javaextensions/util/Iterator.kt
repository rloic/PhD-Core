package javaextensions.util

fun <T, U> Iterator<T>.map(fn: (T) -> U): Iterator<U> = object : Iterator<U> {
    override fun hasNext() = this@map.hasNext()
    override fun next() = fn(this@map.next())
}
