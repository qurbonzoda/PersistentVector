package immutableVector

class TrieIterator<out T>(root: Array<Any?>,
                          val size: Int,
                          private val height: Int,
                          private val logMaxBufferSize: Int): Iterator<T> {
    private val path: Array<Any?> = arrayOfNulls<Any?>(this.height)
    private var index = 0
    private val maxBufferSizeMinusOne = (1 shl this.logMaxBufferSize) - 1

    init {
        this.path[0] = root
        this.fillWithLeftmost(1)
    }

    private fun fillWithLeftmost(start: Int) {
        for (i in start until this.height) {
            this.path[i] = (this.path[i - 1] as Array<Any?>)[0]
        }
    }

    override fun hasNext(): Boolean {
        return this.index < this.size
    }

    private fun indexAtShift(shift: Int) = (this.index shr shift) and this.maxBufferSizeMinusOne

    override fun next(): T {
        if (!this.hasNext()) {
            throw NoSuchElementException()
        }

        val leafBufferIndex = this.index and this.maxBufferSizeMinusOne
        val result = (path[this.height - 1] as Array<T>)[leafBufferIndex]

        var shift = 0
        while (this.indexAtShift(shift) == this.maxBufferSizeMinusOne) {
            shift += this.logMaxBufferSize
        }

        if (shift > 0 && this.index + 1 < this.size) {
            val level = this.height - 1 - shift / this.logMaxBufferSize
            this.path[level + 1] = (this.path[level] as Array<Any?>)[this.indexAtShift(shift) + 1]
            fillWithLeftmost(level + 2)
        }

        this.index += 1

        return result
    }
}