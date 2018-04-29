package immutableVector.sizeNotInBuffer.fixedHeight.growableBufferSize.bufferSize64

import immutableVector.BigVectorIterator
import immutableVector.ImmutableVector

internal class BigVector<T>(private val rest: Array<Any?>,
                            private val last: Array<T>,
                            override val size: Int) : ImmutableVector<T> {
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
            return BigVector(this.rest, newLast, this.size + 1)
        }

        if (lastSize < MAX_BUFFER_SIZE) {
            val newLast = arrayOfNulls<Any?>(this.last.size shl 1) as Array<T>
            System.arraycopy(this.last, 0, newLast, 0, this.last.size)
            newLast[lastSize] = e
            return BigVector(this.rest, newLast, this.size + 1)
        }

        val newRest = pushLast(SHIFT_START, this.rest, this.last)
        val newLast = arrayOf<Any?>(e) as Array<T>
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

    override fun set(index: Int, e: T): ImmutableVector<T> {
        if (index < 0 || index >= this.size) {
            throw IndexOutOfBoundsException()
        }

        if (this.lastOff() <= index) {
            val newLast = this.last.copyOf()
            newLast[index and MAX_BUFFER_SIZE_MINUS_ONE] = e
            return BigVector(this.rest, newLast, this.size)
        }

        val newRest = setInRest(this.rest, SHIFT_START, index, e)
        return BigVector(newRest, this.last, this.size)
    }

    private fun setInRest(restNode: Array<Any?>, shift: Int, index: Int, e: T): Array<Any?> {
        val bufferIndex = (index shr shift) and MAX_BUFFER_SIZE_MINUS_ONE
        val newRestNode = restNode.copyOf()
        if (shift == 0) {
            newRestNode[bufferIndex] = e
        } else {
            newRestNode[bufferIndex] = setInRest(newRestNode[bufferIndex] as Array<Any?>,
                    shift - LOG_MAX_BUFFER_SIZE, index, e)
        }
        return newRestNode
    }

    override fun iterator(): Iterator<T> {
        return BigVectorIterator(this.rest, this.last, this.size, REST_HEIGHT, LOG_MAX_BUFFER_SIZE)
    }
}