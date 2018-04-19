import immutableVector.TwoBufferIterator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TwoBufferIteratorTests {
    @Test
    fun emptyIteratorTest() {
        val emptyIterator = TwoBufferIterator<Int>(emptyArray(), emptyArray(), 0)

        assertFalse(emptyIterator.hasNext())
        assertThrows(NoSuchElementException::class.java) {
            emptyIterator.next()
        }
    }


    @Test
    fun simpleTest() {
        val firstBuffer = arrayOf(1, 2, 3, 4)
        val secondBuffer = arrayOf(1, 2, 3, 4)
        val iterator = TwoBufferIterator(firstBuffer, secondBuffer, 8)

        repeat(times = 8) { it ->
            assertTrue(iterator.hasNext())
            assertEquals((it % 4) + 1, iterator.next())
        }

        assertFalse(iterator.hasNext())
        assertThrows(NoSuchElementException::class.java) {
            iterator.next()
        }
    }

    @Test
    fun biggerThanSizeBufferTest() {
        val firstBuffer = arrayOf(1, 2, 3, 4)
        val secondBuffer = arrayOf(1, 2, 3, 4)
        val iterator = TwoBufferIterator(firstBuffer, secondBuffer, 6)

        repeat(times = 6) { it ->
            assertTrue(iterator.hasNext())
            assertEquals((it % 4) + 1, iterator.next())
        }

        assertFalse(iterator.hasNext())
        assertThrows(NoSuchElementException::class.java) {
            iterator.next()
        }
    }
}