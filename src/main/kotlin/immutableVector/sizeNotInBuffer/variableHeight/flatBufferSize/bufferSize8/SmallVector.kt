package immutableVector.sizeNotInBuffer.variableHeight.flatBufferSize.bufferSize8

import immutableVector.ImmutableVector

internal class SmallVector<T>(private val buffer: Array<T>) : ImmutableVector<T> {
    override val size: Int
        get() = this.buffer.size

    override fun addLast(e: T): ImmutableVector<T> {
        if (this.buffer.size == MAX_BUFFER_SIZE) {
            val rhs = arrayOfNulls<Any?>(1) as Array<T>
            rhs[0] = e
            return MidVector(this.buffer, rhs)
        }

        val newBuffer = arrayOfNulls<Any?>(this.size + 1)
        System.arraycopy(this.buffer, 0, newBuffer, 0, this.size)
        newBuffer[this.size] = e
        return SmallVector(newBuffer as Array<T>)
    }

    override fun get(index: Int): T {
        return this.buffer[index]
    }

    override fun set(index: Int, e: T): ImmutableVector<T> {
        if (index >= this.size) {
            throw IndexOutOfBoundsException()
        }
        val newBuffer = this.buffer.copyOf()
        newBuffer[index] = e
        return SmallVector(newBuffer)
    }

    override fun iterator(): Iterator<T> {
        return this.buffer.iterator()
    }
}