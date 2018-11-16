package lesson3

import java.util.SortedSet
import kotlin.test.*

abstract class AbstractHeadTailTest {
    private lateinit var tree: SortedSet<Int>

    protected fun fillTree(empty: SortedSet<Int>) {
        this.tree = empty
        //В произвольном порядке добавим числа от 1 до 10
        tree.add(5)
        tree.add(1)
        tree.add(2)
        tree.add(7)
        tree.add(9)
        tree.add(10)
        tree.add(8)
        tree.add(4)
        tree.add(3)
        tree.add(6)
    }

    protected fun bigTree(empty: KtBinaryTree<Int>): KtBinaryTree<Int> {
        val tree = empty
        for (i in 1..10000) {
            tree.add(i)
        }
        return tree
    }

    protected fun doHeadSetTest() {
        var set: SortedSet<Int> = tree.headSet(5)
        assertEquals(true, set.contains(1))
        assertEquals(true, set.contains(2))
        assertEquals(true, set.contains(3))
        assertEquals(true, set.contains(4))
        assertEquals(false, set.contains(5))
        assertEquals(false, set.contains(6))
        assertEquals(false, set.contains(7))
        assertEquals(false, set.contains(8))
        assertEquals(false, set.contains(9))
        assertEquals(false, set.contains(10))


        set = tree.headSet(127)
        for (i in 1..10)
            assertEquals(true, set.contains(i))

    }

    protected fun doTailSetTest() {
        var set: SortedSet<Int> = tree.tailSet(5)
        assertEquals(false, set.contains(1))
        assertEquals(false, set.contains(2))
        assertEquals(false, set.contains(3))
        assertEquals(false, set.contains(4))
        assertEquals(true, set.contains(5))
        assertEquals(true, set.contains(6))
        assertEquals(true, set.contains(7))
        assertEquals(true, set.contains(8))
        assertEquals(true, set.contains(9))
        assertEquals(true, set.contains(10))

        set = tree.tailSet(-128)
        for (i in 1..10)
            assertEquals(true, set.contains(i))

    }

    protected fun doHeadSetRelationTest() {
        val set: SortedSet<Int> = tree.headSet(7)
        assertEquals(6, set.size)
        assertEquals(10, tree.size)
        tree.add(0)
        assertTrue(set.contains(0))
        set.remove(4)
        assertFalse(tree.contains(4))
        tree.remove(6)
        assertFalse(set.contains(6))
        tree.add(12)
        assertFalse(set.contains(12))
        assertEquals(5, set.size)
        assertEquals(10, tree.size)
    }

    protected fun doTailSetRelationTest() {
        val set: SortedSet<Int> = tree.tailSet(4)
        assertEquals(7, set.size)
        assertEquals(10, tree.size)
        tree.add(12)
        assertTrue(set.contains(12))
        set.remove(4)
        assertFalse(tree.contains(4))
        tree.remove(6)
        assertFalse(set.contains(6))
        tree.add(0)
        assertFalse(set.contains(0))
        assertEquals(6, set.size)
        assertEquals(10, tree.size)
    }

    protected fun doSubSetTest() {
        var set: SortedSet<Int> = tree.subSet(4, 8)
        assertEquals(false, set.contains(1))
        assertEquals(false, set.contains(2))
        assertEquals(false, set.contains(3))
        assertEquals(true, set.contains(4))
        assertEquals(true, set.contains(5))
        assertEquals(true, set.contains(6))
        assertEquals(true, set.contains(7))
        assertEquals(true, set.contains(8))
        assertEquals(false, set.contains(9))
        assertEquals(false, set.contains(10))
        set = tree.subSet(1, 2)
        assertEquals(true, set.contains(1))
        assertEquals(true, set.contains(2))
        assertEquals(false, set.contains(3))
        assertEquals(false, set.contains(4))
        assertEquals(false, set.contains(5))
        assertEquals(false, set.contains(6))
        assertEquals(false, set.contains(7))
        assertEquals(false, set.contains(8))
        assertEquals(false, set.contains(9))
        assertEquals(false, set.contains(10))
        set = tree.subSet(1, 10)
        assertEquals(true, set.contains(1))
        assertEquals(true, set.contains(2))
        assertEquals(true, set.contains(3))
        assertEquals(true, set.contains(4))
        assertEquals(true, set.contains(5))
        assertEquals(true, set.contains(6))
        assertEquals(true, set.contains(7))
        assertEquals(true, set.contains(8))
        assertEquals(true, set.contains(9))
        assertEquals(true, set.contains(10))
        set = tree.subSet(7, 8)
        assertEquals(false, set.contains(1))
        assertEquals(false, set.contains(2))
        assertEquals(false, set.contains(3))
        assertEquals(false, set.contains(4))
        assertEquals(false, set.contains(5))
        assertEquals(false, set.contains(6))
        assertEquals(true, set.contains(7))
        assertEquals(true, set.contains(8))
        assertEquals(false, set.contains(9))
        assertEquals(false, set.contains(10))
    }

    protected fun doSubSetRelationTest() {
        val set: SortedSet<Int> = tree.subSet(4, 12)
        assertEquals(7, set.size)
        assertEquals(10, tree.size)
        tree.add(11)
        assertTrue(set.contains(11))
        set.remove(4)
        assertFalse(set.contains(4))
        tree.remove(6)
        assertFalse(set.contains(6))
        tree.add(0)
        assertFalse(set.contains(0))
        assertEquals(6, set.size)
        assertEquals(10, tree.size)
    }

    protected fun doBigTest() {
        val test = bigTree(KtBinaryTree())
        var set = test.tailSet(566)
        assertEquals(9435, set.size)
        for (i in 1..565) {
            assertEquals(false, set.contains(i))
        }
        for (i in 566..10000) {
            assertEquals(true, set.contains(i))
        }
        set = test.headSet(5001)
        assertEquals(5000, set.size)
        for (i in 1..5000) {
            assertEquals(true, set.contains(i))
        }
        for (i in 5001..10000) {
            assertEquals(false, set.contains(i))
        }
        set = test.subSet(1, 9999)
        assertEquals(9999, set.size)
        for (i in 1..9999) {
            assertEquals(true, set.contains(i))
        }
        assertEquals(false, set.contains(10000))
    }
}