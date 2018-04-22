package immutableVector.sizeNotInBuffer.fixedHeight.fixedBufferSize.bufferSize16

import immutableVector.ImmutableVector
import immutableVector.TwoBufferIterator

internal class MidVector<T>(private val lhs: Array<T>,
                            private val rhs: Array<T>,
                            override val size: Int) : ImmutableVector<T> {
    override fun addLast(e: T): ImmutableVector<T> {
        if (this.size shr 1 == MAX_BUFFER_SIZE) {
            var rest = arrayOfNulls<Any?>(MAX_BUFFER_SIZE)
            rest[0] = this.lhs
            rest[1] = this.rhs
            repeat(REST_HEIGHT - 2) {
                val newRest = arrayOfNulls<Any?>(MAX_BUFFER_SIZE)
                newRest[0] = rest
                rest = newRest
            }
            val last = arrayOfNulls<Any?>(MAX_BUFFER_SIZE)
            last[0] = e
            return BigVector(rest, last as Array<T>, this.size + 1)
        }

        val newRhs = arrayOfNulls<Any?>(MAX_BUFFER_SIZE) as Array<T>
        System.arraycopy(this.rhs, 0, newRhs, 0, MAX_BUFFER_SIZE)
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