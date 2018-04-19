import immutableVector.BufferIterator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BufferIteratorTests {
    @Test
    fun emptyBufferIteratorTest() {
        val emptyIterator = BufferIterator<Int>(emptyArray(), 0)

        assertFalse(emptyIterator.hasNext())
        assertThrows(NoSuchElementException::class.java) {
            emptyIterator.next()
        }
    }

    @Test
    fun simpleTest() {
        val bufferIterator = BufferIterator(arrayOf(1, 2, 3, 4, 5), 5)

        repeat(times = 5) { it ->
            assertTrue(bufferIterator.hasNext())
            assertEquals(it + 1, bufferIterator.next())
        }

        assertFalse(bufferIterator.hasNext())
        assertThrows(NoSuchElementException::class.java) {
            bufferIterator.next()
        }
    }

    @Test
    fun biggerThanSizeBufferTest() {
        val bufferIterator = BufferIterator(arrayOf(1, 2, 3, 4, 5), 3)

        repeat(times = 3) { it ->
            assertTrue(bufferIterator.hasNext())
            assertEquals(it + 1, bufferIterator.next())
        }

        assertFalse(bufferIterator.hasNext())
        assertThrows(NoSuchElementException::class.java) {
            bufferIterator.next()
        }
    }
}