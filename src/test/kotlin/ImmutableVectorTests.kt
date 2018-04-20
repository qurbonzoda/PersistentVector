import immutableVector.ImmutableVector
import immutableVector.sizeNotInBuffer.variableHeight.fixedBufferSize.bufferSize8.emptyVector
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*


class ImmutableVectorTests {
    // TODO: add tests for ImmutableVector of optional values. e.g. ImmutableVector<Int?>

    private fun <T> ImmutableVector<T>.isEmpty(): Boolean {
        return this.size == 0
    }

    @Test
    fun isEmptyTests() {
        var vector = emptyVector<String>()

        assertTrue(vector.isEmpty())
        assertFalse(vector.addLast("last").isEmpty())

        val elementsToAdd = 100000
        repeat(times = elementsToAdd) { index ->
            vector = vector.addLast(index.toString())
            assertFalse(vector.isEmpty())
        }
    }

    @Test
    fun sizeTests() {
        var vector = emptyVector<Int>()

        assertTrue(vector.size == 0)
        assertEquals(1, vector.addLast(1).size)

        val elementsToAdd = 100000
        repeat(times = elementsToAdd) { index ->
            vector = vector.addLast(index)
            assertEquals(index + 1, vector.size)
        }
    }

    @Test
    fun toListTest() {
        var vector = emptyVector<Int>()

        assertEquals(emptyList<Int>(), vector.toList())
        assertEquals(listOf(1), vector.addLast(1).toList())

        assertEquals(
                listOf(1, 2, 3, 4, 5, 6),
                vector
                        .addLast(1).addLast(2).addLast(3).addLast(4).addLast(5)
                        .addLast(6)
                        .toList()
        )

        assertEquals(
                listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20),
                vector
                        .addLast(1).addLast(2).addLast(3).addLast(4).addLast(5)
                        .addLast(6).addLast(7).addLast(8).addLast(9).addLast(10)
                        .addLast(11).addLast(12).addLast(13).addLast(14).addLast(15)
                        .addLast(16).addLast(17).addLast(18).addLast(19).addLast(20)
                        .toList()
        )

        val elementsToAdd = 1000
        val list = LinkedList<Int>()
        repeat(times = elementsToAdd) { index ->
            list.addLast(index)
            vector = vector.addLast(index)
            assertEquals(list, vector.toList())
        }
    }

    @Test
    fun addLastTests() {
        var vector = emptyVector<Int>()

        assertEquals(1, vector.addLast(1).get(0))

        val elementsToAdd = 10000
        repeat(times = elementsToAdd) { index ->
            vector = vector.addLast(index)

            assertEquals(0, vector.get(0))
            assertEquals(index, vector.get(index))
            assertEquals(index + 1, vector.size)
            assertEquals(List(index + 1) { it }, vector.toList())
        }
    }

    @Test
    fun getTests() {
        var vector = emptyVector<Int>()

        assertThrows(IndexOutOfBoundsException::class.java) {
            vector.get(0)
        }
        assertEquals(1, vector.addLast(1).get(0))

        val elementsToAdd = 10000
        repeat(times = elementsToAdd) { index ->
            vector = vector.addLast(index)

            for (i in 0..index) {
                assertEquals(i, vector.get(i))
            }
        }
    }

    @Test
    fun randomOperationsTests() {
        val random = Random()
        val lists = mutableListOf(emptyList<Int>())
        val vectors = mutableListOf(emptyVector<Int>())

        repeat(times = 100000) {
            val quarter = (lists.size + 3) / 4

            val index = (lists.size - quarter) + random.nextInt(quarter)
            val list = lists[index]
            val vector = vectors[index]

            val newList: List<Int>
            val newVector: ImmutableVector<Int>

            val value = random.nextInt()
            newList = list + listOf(value)
            newVector = vector.addLast(value)

            assertEquals(newList, newVector.toList())

            vectors.add(newVector)
            lists.add(newList)
        }

//        println(lists.maxBy { it.size }?.size)
    }

    @Test
    fun randomOperationsFastTests() {
        repeat(times = 10) {

            val random = Random()
            val lists = List(20) { LinkedList<Int>() }
            val vectors = MutableList(20) { emptyVector<Int>() }

            repeat(times = 1000000) {
                val index = random.nextInt(lists.size)
                val list = lists[index]
                val vector = vectors[index]

                val value = random.nextInt()
                list.addLast(value)
                val newVector = vector.addLast(value)

                assertEquals(list.isEmpty(), newVector.isEmpty())
                assertEquals(list.firstOrNull(), newVector.get(0))
                assertEquals(list.lastOrNull(), newVector.get(vector.size))
                assertEquals(list.size, newVector.size)

                vectors[index] = newVector
            }

//            println(lists.maxBy { it.size }?.size)
        }
    }
}