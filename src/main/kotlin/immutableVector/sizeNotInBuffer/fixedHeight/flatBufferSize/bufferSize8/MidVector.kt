package immutableVector.sizeNotInBuffer.fixedHeight.flatBufferSize.bufferSize8

import immutableVector.ImmutableVector
import immutableVector.TwoBufferIterator

internal class MidVector<T>(private val lhs: Array<T>,
                            private val rhs: Array<T>) : ImmutableVector<T> {
    override val size: Int
        get() = MAX_BUFFER_SIZE + this.rhs.size

    override fun addLast(e: T): ImmutableVector<T> {
        if (this.rhs.size == MAX_BUFFER_SIZE) {
            var rest = arrayOf<Any?>(this.lhs, this.rhs)
            repeat(REST_HEIGHT - 2) {
                rest = arrayOf(rest)
            }
            val last = arrayOf<Any?>(e)
            return BigVector(rest, last as Array<T>, this.size + 1)
        }

        val newRhs = arrayOfNulls<Any?>(this.rhs.size + 1)
        System.arraycopy(this.rhs, 0, newRhs, 0, this.rhs.size)
        newRhs[this.rhs.size] = e
        return MidVector(this.lhs, newRhs as Array<T>)
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
            return MidVector(newLhs, this.rhs)
        }
        val newRhs = this.rhs.copyOf()
        newRhs[index - MAX_BUFFER_SIZE] = e
        return MidVector(this.lhs, newRhs)
    }

    override fun iterator(): Iterator<T> {
        return TwoBufferIterator(this.lhs, this.rhs, this.size)
    }
}