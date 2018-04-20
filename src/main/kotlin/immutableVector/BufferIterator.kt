package immutableVector

import java.util.*

internal class BufferIterator<out T>(private val buffer: Array<T>,
                                     private val size: Int) : Iterator<T> {
    private var index = 0

    override fun hasNext(): Boolean {
        return this.index < this.size
    }

    override fun next(): T {
        if (!this.hasNext()) {
            throw NoSuchElementException()
        }
        return this.buffer[this.index++]
    }

}