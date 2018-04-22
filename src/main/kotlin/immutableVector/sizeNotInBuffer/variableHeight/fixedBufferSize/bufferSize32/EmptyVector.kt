package immutableVector.sizeNotInBuffer.variableHeight.fixedBufferSize.bufferSize32

import immutableVector.ImmutableVector
import java.util.*

private object EmptyVector : ImmutableVector<Any?> {
    override val size = 0

    override fun addLast(e: Any?): ImmutableVector<Any?> {
        val buffer = arrayOfNulls<Any?>(MAX_BUFFER_SIZE)
        buffer[0] = e
        return SmallVector(buffer, 1)
    }

    override fun get(index: Int): Any? {
        throw IndexOutOfBoundsException()
    }

    override fun iterator(): Iterator<Any?> {
        return Collections.emptyIterator()
    }
}

fun <T> emptyVector(): ImmutableVector<T> {
    return EmptyVector as ImmutableVector<T>
}