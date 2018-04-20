package immutableVector.sizeNotInBuffer.variableHeight.fixedBufferSize.bufferSize8

import immutableVector.BufferIterator
import immutableVector.ImmutableVector

internal class SmallVector<T>(private val buffer: Array<T>, override val size: Int) : ImmutableVector<T> {
    override fun addLast(e: T): ImmutableVector<T> {
        val newBuffer = arrayOfNulls<Any?>(MAX_BUFFER_SIZE) as Array<T>
        if (this.size == MAX_BUFFER_SIZE) {
            newBuffer[0] = e
            return MidVector(this.buffer, newBuffer, this.size + 1)
        }

        System.arraycopy(this.buffer, 0, newBuffer, 0, MAX_BUFFER_SIZE)
        newBuffer[this.size] = e
        return SmallVector(newBuffer, this.size + 1)
    }

    override fun get(index: Int): T {
        if (index >= this.size) {
            throw IndexOutOfBoundsException()
        }
        return this.buffer[index]
    }

    override fun iterator(): Iterator<T> {
        return BufferIterator(this.buffer, this.size)
    }
}