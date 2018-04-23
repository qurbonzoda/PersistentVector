package immutableVector.sizeNotInBuffer.fixedHeight.growableBufferSize.bufferSize64

import immutableVector.BufferIterator
import immutableVector.ImmutableVector

internal class SmallVector<T>(private val buffer: Array<T>, override val size: Int) : ImmutableVector<T> {
    override fun addLast(e: T): ImmutableVector<T> {
        if (this.size < this.buffer.size) {
            val newBuffer = this.buffer.copyOf()
            newBuffer[this.size] = e
            return SmallVector(newBuffer, this.size + 1)
        }
        if (this.buffer.size < MAX_BUFFER_SIZE) {
            val newBuffer = arrayOfNulls<Any?>(this.buffer.size shl 1)
            System.arraycopy(this.buffer, 0, newBuffer, 0, this.buffer.size)
            newBuffer[this.size] = e
            return SmallVector(newBuffer as Array<T>, this.size + 1)
        }
        return MidVector(this.buffer, arrayOf<Any?>(e) as Array<T>, this.size + 1)
    }

    override fun get(index: Int): T {
        return this.buffer[index]
    }

    override fun iterator(): Iterator<T> {
        return BufferIterator(this.buffer, this.size)
    }
}