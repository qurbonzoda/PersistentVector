package immutableVector.sizeNotInBuffer.fixedHeight.growableBufferSize.bufferSize64

import immutableVector.ImmutableVector
import immutableVector.TwoBufferIterator

internal class MidVector<T>(private val lhs: Array<T>,
                            private val rhs: Array<T>,
                            override val size: Int) : ImmutableVector<T> {
    override fun addLast(e: T): ImmutableVector<T> {
        if (this.size shr 1 == MAX_BUFFER_SIZE) {
            var rest = arrayOf<Any?>(this.lhs, this.rhs)
            repeat(REST_HEIGHT - 2) {
                rest = arrayOf(rest)
            }
            val last = arrayOf<Any?>(e) as Array<T>
            return BigVector(rest, last, this.size + 1)
        }

        if (this.size - MAX_BUFFER_SIZE == this.rhs.size) {
            val newRhs = arrayOfNulls<Any?>(this.rhs.size shl 1) as Array<T>
            System.arraycopy(this.rhs, 0, newRhs, 0, this.rhs.size)
            newRhs[this.rhs.size] = e
            return MidVector(this.lhs, newRhs, this.size + 1)
        }

        val newRhs = this.rhs.copyOf()
        newRhs[this.size - MAX_BUFFER_SIZE] = e
        return MidVector(this.lhs, newRhs, this.size + 1)
    }

    override fun get(index: Int): T {
        if (index >= this.size) {
            throw IndexOutOfBoundsException()
        }
        if (index < MAX_BUFFER_SIZE) {
            return this.lhs[index]
        }
        return this.rhs[index - MAX_BUFFER_SIZE]
    }

    override fun set(index: Int, e: T): ImmutableVector<T> {
        if (index >= this.size) {
            throw IndexOutOfBoundsException()
        }
        if (index < MAX_BUFFER_SIZE) {
            val newLhs = this.lhs.copyOf()
            newLhs[index] = e
            return MidVector(newLhs, this.rhs, this.size)
        }
        val newRhs = this.rhs.copyOf()
        newRhs[index - MAX_BUFFER_SIZE] = e
        return MidVector(this.lhs, newRhs, this.size)
    }

    override fun iterator(): Iterator<T> {
        return TwoBufferIterator(this.lhs, this.rhs, this.size)
    }
}