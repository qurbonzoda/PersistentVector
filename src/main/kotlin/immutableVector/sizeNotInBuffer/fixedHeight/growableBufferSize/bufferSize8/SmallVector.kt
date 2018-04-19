package immutableVector.sizeNotInBuffer.fixedHeight.growableBufferSize.bufferSize8

import immutableVector.BufferIterator
import immutableVector.ImmutableVector

class SmallVector<T>(private val buffer: Array<T>, override val size: Int): ImmutableVector<T> {
    override fun addLast(e: T): ImmutableVector<T> {
        return if (this.buffer.size > this.size) {
            val newBuffer = arrayOfNulls<Any?>(this.buffer.size)
            System.arraycopy(this.buffer, 0, newBuffer, 0, this.buffer.size)
            newBuffer[this.size] = e
            SmallVector(newBuffer as Array<T>, this.size + 1)
        } else {
            val newBuffer = arrayOfNulls<Any?>(this.buffer.size shl 1)
            System.arraycopy(this.buffer, 0, newBuffer, 0, this.buffer.size)
            newBuffer[this.size] = e
            SmallVector(newBuffer as Array<T>, this.size + 1)
        }
    }

    override fun get(index: Int): T {
        return this.buffer[index]
    }

    override fun iterator(): Iterator<T> {
        return BufferIterator(this.buffer, this.size)
    }
}