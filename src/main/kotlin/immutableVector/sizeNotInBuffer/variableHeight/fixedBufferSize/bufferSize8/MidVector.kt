package immutableVector.sizeNotInBuffer.variableHeight.fixedBufferSize.bufferSize8

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
            val rest = this.bufferWithOnlyElement(this.lhs)
            rest[1] = this.rhs
            val last = this.bufferWithOnlyElement(e) as Array<T>
            return BigVector(rest, last, this.size + 1, LOG_MAX_BUFFER_SIZE)
        }

        val newRhs = this.copyBuffer(this.rhs)
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
            val newLhs = this.copyBuffer(this.lhs)
            newLhs[index] = e
            return MidVector(newLhs, this.rhs, this.size)
        }
        val newRhs = this.copyBuffer(this.rhs)
        newRhs[index - MAX_BUFFER_SIZE] = e
        return MidVector(this.lhs, newRhs, this.size)
    }

    override fun iterator(): Iterator<T> {
        return TwoBufferIterator(this.lhs, this.rhs, this.size)
    }
}