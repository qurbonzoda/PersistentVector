package immutableVector.sizeNotInBuffer.variableHeight.flatBufferSize.bufferSize16

import immutableVector.ImmutableVector
import immutableVector.TwoBufferIterator

internal class MidVector<T>(private val lhs: Array<T>,
                            private val rhs: Array<T>) : ImmutableVector<T> {
    override val size: Int
        get() = this.rhs.size + this.lhs.size

    override fun addLast(e: T): ImmutableVector<T> {
        if (this.rhs.size == MAX_BUFFER_SIZE) {
            val rest = arrayOfNulls<Any?>(MAX_BUFFER_SIZE)
            rest[0] = this.lhs
            rest[1] = this.rhs
            val last = arrayOfNulls<Any?>(1) as Array<T>
            last[0] = e
            return BigVector(rest, last, this.size + 1, LOG_MAX_BUFFER_SIZE)
        }

        val newRhs = arrayOfNulls<Any?>(this.rhs.size + 1) as Array<T>
        System.arraycopy(this.rhs, 0, newRhs, 0, this.rhs.size)
        newRhs[this.rhs.size] = e
        return MidVector(this.lhs, newRhs)
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