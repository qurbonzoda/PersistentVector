package immutableVector.sizeNotInBuffer.fixedHeight.flatBufferSize.bufferSize8

import immutableVector.ImmutableVector
import java.util.*

private object EmptyVector : ImmutableVector<Any?> {
    override val size = 0

    override fun addLast(e: Any?): ImmutableVector<Any?> {
        return SmallVector(arrayOf(e))
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