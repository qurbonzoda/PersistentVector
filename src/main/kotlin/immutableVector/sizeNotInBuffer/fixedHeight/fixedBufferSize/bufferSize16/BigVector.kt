package immutableVector.sizeNotInBuffer.fixedHeight.fixedBufferSize.bufferSize16

import immutableVector.BigVectorIterator
import immutableVector.ImmutableVector

internal class BigVector<T>(private val rest: Array<Any?>,
                            private val last: Array<T>,
                            override val size: Int) : ImmutableVector<T> {
    private fun lastOff(): Int {
        return ((this.size - 1) shr LOG_MAX_BUFFER_SIZE) shl LOG_MAX_BUFFER_SIZE
    }

    private fun <U> copyBuffer(buffer: Array<U>): Array<U> {
        return buffer.copyOf()
    }

    private fun bufferWithOnlyElement(e: Any?): Array<Any?> {
        val buffer = arrayOfNulls<Any?>(MAX_BUFFER_SIZE)
        buffer[0] = e
        return buffer
    }

    private fun pushLast(shift: Int, restNode: Array<Any?>?, last: Array<T>): Array<Any?> {
        val index = ((this.size - 1) shr shift) and MAX_BUFFER_SIZE_MINUS_ONE
        val newRestNode = if (restNode != null) {
            this.copyBuffer(restNode)
        } else {
            arrayOfNulls<Any?>(MAX_BUFFER_SIZE)
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
        if (lastSize < MAX_BUFFER_SIZE) {
            val newLast = this.copyBuffer(this.last)
            newLast[lastSize] = e
            return BigVector(this.rest, newLast, this.size + 1)
        }

        val newRest = pushLast(SHIFT_START, this.rest, this.last)
        val newLast = bufferWithOnlyElement(e) as Array<T>
        return BigVector(newRest, newLast, this.size + 1)
    }

    private fun bufferFor(index: Int): Array<T> {
        val lastOff = this.lastOff()
        if (lastOff <= index) {
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