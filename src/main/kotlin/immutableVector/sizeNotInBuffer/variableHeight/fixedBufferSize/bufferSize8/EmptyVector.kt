package immutableVector.sizeNotInBuffer.variableHeight.fixedBufferSize.bufferSize8

import immutableVector.ImmutableVector
import java.util.*

private object EmptyVector : ImmutableVector<Any?> {
    override val size = 0

    private fun bufferWithOnlyElement(e: Any?): Array<Any?> {
        val buffer = arrayOfNulls<Any?>(MAX_BUFFER_SIZE)
        buffer[0] = e
        return buffer
    }

    override fun addLast(e: Any?): ImmutableVector<Any?> {
        val buffer = this.bufferWithOnlyElement(e)
        return SmallVector(buffer, 1)
    }

    override fun get(index: Int): Any? {
        throw IndexOutOfBoundsException()
    }

    override fun set(index: Int, e: Any?): ImmutableVector<Any?> {
        throw IndexOutOfBoundsException()
    }

    override fun iterator(): Iterator<Any?> {
        return Collections.emptyIterator()
    }
}

fun <T> emptyVector(): ImmutableVector<T> {
    return EmptyVector as ImmutableVector<T>
}