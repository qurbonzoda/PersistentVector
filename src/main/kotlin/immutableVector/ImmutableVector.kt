package immutableVector

interface ImmutableVector<T> : Iterable<T> {
    val size: Int
    fun addLast(e: T): ImmutableVector<T>
    fun get(index: Int): T
    fun set(index: Int, e: T): ImmutableVector<T>
}