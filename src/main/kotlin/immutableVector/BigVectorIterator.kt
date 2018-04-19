package immutableVector

class BigVectorIterator<out T>(rest: Array<Any?>,
                               private val last: Array<T>,
                               private val size: Int,
                               restHeight: Int,
                               logMaxBufferSize: Int): Iterator<T> {
    private var index = 0
    private val trieIterator = TrieIterator<T>(rest, ((this.size - 1) shr logMaxBufferSize) shl logMaxBufferSize,
            restHeight, logMaxBufferSize)

    override fun hasNext(): Boolean {
        return this.index < this.size
    }

    override fun next(): T {
        if (!this.hasNext()) {
            throw NoSuchElementException()
        }
        if (this.trieIterator.hasNext()) {
            this.index++
            return this.trieIterator.next()
        }
        return this.last[this.index++ - this.trieIterator.size]
    }
}