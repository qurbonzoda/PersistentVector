package immutableVector.sizeNotInBuffer.fixedHeight.fixedBufferSize.bufferSize16

import immutableVector.ImmutableVector
import immutableVector.TwoBufferIterator

internal class MidVector<T>(private val lhs: Array<T>,
                            private val rhs: Array<T>,
                            override val size: Int) : ImmutableVector<T> {
    private fun <U> copyBuffer(buffer: Array<U>): Array<U> {
        return buffer.copyOf()
    }

    private fun bufferWithOnlyElement(e: Any?): Array<Any?> {
        val buffer = arrayOfNulls<Any?>(MAX_BUFFER_SIZE)
        buffer[0] = e
        return buffer
    }

    override fun addLast(e: T): ImmutableVector<T> {
        if (this.size shr 1 == MAX_BUFFER_SIZE) {
            var rest = this.bufferWithOnlyElement(this.lhs)
            rest[1] = this.rhs
            repeat(REST_HEIGHT - 2) {
                rest = this.bufferWithOnlyElement(rest)
            }
            val last = bufferWithOnlyElement(e)
            return BigVector(rest, last as Array<T>, this.size + 1)
        }

        val newRhs = this.copyBuffer(this.rhs)
        newRhs[this.size - MAX_BUFFER_SIZE] = e
        return MidVector(this.lhs, newRhs, this.size + 1)
    }

    override fun get(index: Int): T {
        if (index > this.size) {
            throw IndexOutOfBoundsException()
        }
        if (index < MAX_BUFFER_SIZE) {
            return this.lhs[index]
        }
        return this.rhs[index - MAX_BUFFER_SIZE]
    }

    override fun iterator(): Iterator<T> {
        return TwoBufferIterator(this.lhs, this.rhs, this.size)
    }
}