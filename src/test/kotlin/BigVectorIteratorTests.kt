import immutableVector.BigVectorIterator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class BigVectorIteratorTests {
    @Test
    fun emptyIteratorTest() {
        val emptyIterator = BigVectorIterator<Int>(emptyArray(), emptyArray(), 0, 1, 1)

        assertFalse(emptyIterator.hasNext())
        assertThrows(NoSuchElementException::class.java) {
            emptyIterator.next()
        }
    }

    private fun makeRoot(height: Int, leafCount: Int, logBufferSize: Int): Array<Any?> {
        val bufferSize = 1 shl logBufferSize

        var leaves = arrayListOf<Any?>()
        repeat(leafCount * bufferSize) { it ->
            leaves.add(it)
        }

        repeat(height) {
            val newLeaves = arrayListOf<Any?>()
            for (i in 0 until leaves.size step bufferSize) {
                val buffer = arrayOfNulls<Any?>(bufferSize)
                for (j in i until Math.min(leaves.size, i + bufferSize)) {
                    buffer[j - i] = leaves[j]
                }
                newLeaves.add(buffer)
            }
            leaves = newLeaves
        }

        assert(leaves.size == 1)
        return leaves[0] as Array<Any?>
    }

    @Test
    fun simpleTest() {
        val random = Random()
        for (logMaxBufferSize in 1..6) {
            val bufferSize = 1 shl logMaxBufferSize

            for (height in 1..7) {
                if (random.nextBoolean()) { continue }

                for (leafCount in 1..(1 shl 10)) {
                    if (Math.pow(bufferSize.toDouble(), (height - 1).toDouble()) < leafCount) { break }
                    if (random.nextBoolean()) { continue }

                    val root = makeRoot(height, leafCount, logMaxBufferSize)
                    val rootSize = bufferSize * leafCount

                    for (lastSize in 1..bufferSize) {
                        if (random.nextBoolean()) { continue }

                        val last = Array(lastSize) { it + rootSize }

                        val iterator = BigVectorIterator(root, last, rootSize + lastSize, height, logMaxBufferSize)
                        repeat(rootSize + lastSize) { it ->
                            assertTrue(iterator.hasNext())
                            assertEquals(it, iterator.next())
                        }

                        assertFalse(iterator.hasNext())
                        assertThrows(NoSuchElementException::class.java) {
                            iterator.next()
                        }
                    }
                }
            }
        }
    }
}