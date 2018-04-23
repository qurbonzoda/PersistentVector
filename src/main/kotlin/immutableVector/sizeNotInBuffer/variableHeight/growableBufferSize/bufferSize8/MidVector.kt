package immutableVector.sizeNotInBuffer.variableHeight.growableBufferSize.bufferSize8

import immutableVector.ImmutableVector
import immutableVector.TwoBufferIterator

internal class MidVector<T>(private val lhs: Array<T>,
                            private val rhs: Array<T>,
                            override val size: Int) : ImmutableVector<T> {
    override fun addLast(e: T): ImmutableVector<T> {
        val rhsFilledSize = this.size - MAX_BUFFER_SIZE

        if (rhsFilledSize == MAX_BUFFER_SIZE) {
            val rest = arrayOf<Any?>(this.lhs, this.rhs)
            val last = arrayOf<Any?>(e) as Array<T>
            return BigVector(rest, last, this.size + 1, LOG_MAX_BUFFER_SIZE)
        }

        if (rhsFilledSize == this.rhs.size) {
            val newRhs = arrayOfNulls<Any?>(this.rhs.size shl 1) as Array<T>
            System.arraycopy(this.rhs, 0, newRhs, 0, this.rhs.size)
            newRhs[this.rhs.size] = e
            return MidVector(this.lhs, newRhs, this.size + 1)
        }

        val newRhs = this.rhs.copyOf()
        newRhs[rhsFilledSize] = e
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