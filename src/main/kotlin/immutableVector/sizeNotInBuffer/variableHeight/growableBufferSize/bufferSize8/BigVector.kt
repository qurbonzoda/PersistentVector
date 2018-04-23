package immutableVector.sizeNotInBuffer.variableHeight.growableBufferSize.bufferSize8

import immutableVector.BigVectorIterator
import immutableVector.ImmutableVector

internal class BigVector<T>(private val rest: Array<Any?>,
                            private val last: Array<T>,
                            override val size: Int,
                            private val shiftStart: Int) : ImmutableVector<T> {
    private fun lastOff(): Int {
        return ((this.size - 1) shr LOG_MAX_BUFFER_SIZE) shl LOG_MAX_BUFFER_SIZE
    }

    private fun pushLast(shift: Int, restNode: Array<Any?>?, last: Array<T>): Array<Any?> {
        val index = ((this.size - 1) shr shift) and MAX_BUFFER_SIZE_MINUS_ONE
        val newRestNode: Array<Any?>
        if (restNode != null) {
            if (index < restNode.size) {
                newRestNode = restNode.copyOf()
            } else {
                newRestNode = arrayOfNulls(restNode.size shl 1)
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
        val lastSize = this.size - this.lastOff()

        if (lastSize < this.last.size) {
            val newLast = this.last.copyOf()
            newLast[lastSize] = e
            return BigVector(this.rest, newLast, this.size + 1, this.shiftStart)
        }

        if (lastSize < MAX_BUFFER_SIZE) {
            val newLast = arrayOfNulls<Any?>(this.last.size shl 1) as Array<T>
            System.arraycopy(this.last, 0, newLast, 0, lastSize)
            newLast[lastSize] = e
            return BigVector(this.rest, newLast, this.size + 1, this.shiftStart)
        }

        val newLast = arrayOf<Any?>(e) as Array<T>

        if (this.size shr LOG_MAX_BUFFER_SIZE > 1 shl this.shiftStart) {
            var newRest = arrayOf<Any?>(this.rest, null)
            newRest = pushLast(this.shiftStart + LOG_MAX_BUFFER_SIZE, newRest, this.last)
            return BigVector(newRest, newLast, this.size + 1, this.shiftStart + LOG_MAX_BUFFER_SIZE)
        }

        val newRest = pushLast(this.shiftStart, this.rest, this.last)
        return BigVector(newRest, newLast, this.size + 1, this.shiftStart)
    }

    private fun bufferFor(index: Int): Array<T> {
        val lastOff = this.lastOff()
        if (lastOff <= index) {
            return this.last
        }
        var buffer = this.rest
        for (shift in this.shiftStart downTo 1 step LOG_MAX_BUFFER_SIZE) {
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
        return BigVectorIterator(this.rest, this.last, this.size,
                this.shiftStart / LOG_MAX_BUFFER_SIZE + 1, LOG_MAX_BUFFER_SIZE)
    }
}