package immutableVector.sizeNotInBuffer.fixedHeight.flatBufferSize.bufferSize64

import immutableVector.ImmutableVector

internal class SmallVector<T>(private val buffer: Array<T>) : ImmutableVector<T> {
    override val size: Int
        get() = this.buffer.size

    override fun addLast(e: T): ImmutableVector<T> {
        if (this.buffer.size == MAX_BUFFER_SIZE) {
            val rhs = arrayOf<Any?>(e) as Array<T>
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

    override fun iterator(): Iterator<T> {
        return this.buffer.iterator()
    }
}