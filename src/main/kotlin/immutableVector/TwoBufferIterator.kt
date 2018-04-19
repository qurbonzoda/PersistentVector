package immutableVector

class TwoBufferIterator<out T>(private val first: Array<T>,
                               private val second: Array<T>,
                               private val size: Int): Iterator<T> {
    private var index = 0

    override fun hasNext(): Boolean {
        return this.index < this.size
    }

    override fun next(): T {
        if (!this.hasNext()) {
            throw NoSuchElementException()
        }
        if (this.index < this.first.size) {
            return this.first[this.index++]
        }
        return this.second[this.index++ - this.first.size]
    }
}