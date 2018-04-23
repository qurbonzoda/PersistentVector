package immutableVector.sizeNotInBuffer.fixedHeight.flatBufferSize.bufferSize16

import immutableVector.BigVectorIterator
import immutableVector.ImmutableVector

internal class BigVector<T>(private val rest: Array<Any?>,
                            private val last: Array<T>,
                            override val size: Int) : ImmutableVector<T> {
    private fun pushLast(shift: Int, restNode: Array<Any?>?, last: Array<T>): Array<Any?> {
        val index = ((this.size - 1) shr shift) and MAX_BUFFER_SIZE_MINUS_ONE
        val newRestNode: Array<Any?>
        if (restNode != null) {
            if (index < restNode.size) {
                newRestNode = restNode.copyOf()
            } else {
                newRestNode = arrayOfNulls(restNode.size + 1)
                System.arraycopy(restNode, 0, newRestNode, 0, restNode.size)
            }
        } else {
            newRestNode = arrayOfNulls<Any?>(1)
        }

        if (shift == LOG_MAX_BUFFER_SIZE) {
            newRestNode[index] = last
        } else {
            newRestNode[index] = pushLast(shift - LOG_MAX_BUFFER_SIZE, newRestNode[index] as Array<Any?>?, last)
        }
        return newRestNode
    }

    override fun addLast(e: T): ImmutableVector<T> {
        if (this.last.size < MAX_BUFFER_SIZE) {
            val newLast = arrayOfNulls<Any?>(this.last.size + 1) as Array<T>
            System.arraycopy(this.last, 0, newLast, 0, this.last.size)
            newLast[this.last.size] = e
            return BigVector(this.rest, newLast, this.size + 1)
        }

        val newRest = pushLast(SHIFT_START, this.rest, this.last)
        val newLast = arrayOf<Any?>(e) as Array<T>
        return BigVector(newRest, newLast, this.size + 1)
    }

    private fun bufferFor(index: Int): Array<T> {
        if (this.size - this.last.size <= index) {
            return this.last
        }
        var buffer = this.rest
        for (shift in SHIFT_START downTo 1 step LOG_MAX_BUFFER_SIZE) {
            buffer = buffer[(index shr shift) and MAX_BUFFER_SIZE_MINUS_ONE] as Array<Any?>
        }
        return buffer as Array<T>
    }

    override fun get(index: Int): T {
        if (index < 0 || index >= this.size) {
            throw IndexOutOfBoundsException()
        }
        val buffer = bufferFor(index)
        return buffer[index and MAX_BUFFER_SIZE_MINUS_ONE]
    }

    override fun iterator(): Iterator<T> {
        return BigVectorIterator(this.rest, this.last, this.size, REST_HEIGHT, LOG_MAX_BUFFER_SIZE)
    }
}