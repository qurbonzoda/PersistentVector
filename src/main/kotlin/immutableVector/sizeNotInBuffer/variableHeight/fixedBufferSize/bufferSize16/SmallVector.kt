package immutableVector.sizeNotInBuffer.variableHeight.fixedBufferSize.bufferSize16

import immutableVector.BufferIterator
import immutableVector.ImmutableVector

internal class SmallVector<T>(private val buffer: Array<T>, override val size: Int) : ImmutableVector<T> {
    private fun <U> copyBuffer(buffer: Array<U>): Array<U> {
        return buffer.copyOf()
    }

    private fun bufferWithOnlyElement(e: Any?): Array<Any?> {
        val buffer = arrayOfNulls<Any?>(MAX_BUFFER_SIZE)
        buffer[0] = e
        return buffer
    }

    override fun addLast(e: T): ImmutableVector<T> {
        if (this.size == MAX_BUFFER_SIZE) {
            val newBuffer = this.bufferWithOnlyElement(e) as Array<T>
            return MidVector(this.buffer, newBuffer, this.size + 1)
        }

        val newBuffer = this.copyBuffer(this.buffer)
        newBuffer[this.size] = e
        return SmallVector(newBuffer, this.size + 1)
    }

    override fun get(index: Int): T {
        if (index >= this.size) {
            throw IndexOutOfBoundsException()
        }
        return this.buffer[index]
    }

    override fun set(index: Int, e: T): ImmutableVector<T> {
        if (index >= this.size) {
            throw IndexOutOfBoundsException()
        }
        val newBuffer = this.copyBuffer(this.buffer)
        newBuffer[index] = e
        return SmallVector(newBuffer, this.size)
    }

    override fun iterator(): Iterator<T> {
        return BufferIterator(this.buffer, this.size)
    }
}