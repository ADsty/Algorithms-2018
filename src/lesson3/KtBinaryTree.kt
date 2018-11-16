package lesson3

import java.util.*
import kotlin.NoSuchElementException

// Attention: comparable supported but comparator is not
class KtBinaryTree<T : Comparable<T>>() : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    private var root: Node<T>? = null

    private var first: T? = null

    private var last: T? = null

    override var size = 0
        private set
        get() {
            var result = 0
            val iterator = BinaryTreeIterator()
            while (iterator.hasNext()) {
                val comp = iterator.next()
                when {
                    first != null && last != null && comp >= first!! && comp <= last!! -> result++
                    first != null && last == null && comp >= first!! -> result++
                    last != null && first == null && comp < last!! -> result++
                    first == null && last == null -> result++
                }
            }
            return result
        }

    private class Node<T>(val value: T) {

        var left: Node<T>? = null

        var right: Node<T>? = null
    }

    override fun add(element: T): Boolean {
        val closest = find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        val newNode = Node(element)
        when {
            closest == null -> root = newNode
            comparison < 0 -> {
                assert(closest.left == null)
                closest.left = newNode
            }
            else -> {
                assert(closest.right == null)
                closest.right = newNode
            }
        }
        return true
    }

    override fun checkInvariant(): Boolean =
            root?.let { checkInvariant(it) } ?: true

    private fun checkInvariant(node: Node<T>): Boolean {
        val left = node.left
        if (left != null && (left.value >= node.value || !checkInvariant(left))) return false
        val right = node.right
        return right == null || right.value > node.value && checkInvariant(right)
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */
    override fun remove(element: T): Boolean {
        return remove(element, root, null)
    }

    private fun remove(element: T, node: Node<T>?, father: Node<T>?): Boolean {
        if (node == null) return false
        val compare = element.compareTo(node.value)
        when {
            compare > 0 -> remove(element, node.right, node)
            compare < 0 -> remove(element, node.left, node)
            compare == 0 -> {
                when {
                    node.left == null && node.right == null -> replaceChild(father, node, null)
                    node.right == null -> replaceChild(father, node, node.left)
                    node.left == null -> replaceChild(father, node, node.right)
                    else -> {
                        val change = minimum(node.right!!, node)
                        val rep = Node(change.first.value)
                        if (node.left != null && rep.value != node.left!!.value) rep.left = node.left
                        else rep.left = null
                        if (node.right != null && rep.value != node.right!!.value) {
                            rep.right = node.right
                        } else if (node.right != null && node.right!!.right != null) {
                            rep.right = node.right!!.right
                        } else rep.right = null
                        replaceChild(father, node, rep)
                        if (change.first.right != null) replaceChild(change.second, change.first, change.first.right)
                        else replaceChild(change.second, change.first, null)
                    }
                }
            }
        }
        return true
    }

    private fun minimum(node: Node<T>, parent: Node<T>): Pair<Node<T>, Node<T>> {
        if (node.left == null) return Pair(node, parent)
        return minimum(node.left!!, node)
    }

    private fun replaceChild(parent: Node<T>?, node: Node<T>, newNode: Node<T>?) {
        when {
            parent == null -> root = newNode
            parent.left != null && parent.left!!.value.compareTo(node.value) == 0 -> parent.left = newNode
            else -> parent.right = newNode
        }
    }

    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    private fun find(value: T): Node<T>? {
        return when {
            first != null && last != null && (first!! > value || last!! < value) -> null
            first == null && last != null && last!! <= value -> null
            last == null && first != null && first!! > value -> null
            else -> root?.let { find(it, value) }
        }
    }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }

    inner class BinaryTreeIterator : MutableIterator<T> {

        private var current: Node<T>? = null
        private val queue = ArrayDeque<Node<T>>()

        init {
            pushLeft(root)
        }

        /**
         * Поиск следующего элемента
         * Средняя
         */
        private fun findNext(): Node<T>? {
            val result = queue.pop()
            pushLeft(result.right)
            return result
        }

        private fun pushLeft(node: Node<T>?): Boolean {
            if (node?.value == null) return false
            var current = node
            while (current != null) {
                queue.push(current)
                current = current.left
            }
            return true
        }

        override fun hasNext(): Boolean = queue.peekFirst() != null

        override fun next(): T {
            current = findNext()
            return (current ?: throw NoSuchElementException()).value
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        override fun remove() {
            if (current != null) remove(current!!.value)
            else return
        }
    }

    override fun iterator(): MutableIterator<T> = BinaryTreeIterator()

    override fun comparator(): Comparator<in T>? = null

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        if (root == null) return KtBinaryTree()
        return KtBinaryTree(subOf(fromElement, toElement, root), fromElement, toElement)
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    override fun headSet(toElement: T): SortedSet<T> {
        if (root == null) return KtBinaryTree()
        return KtBinaryTree(firstOf(toElement, root), null, toElement)
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    override fun tailSet(fromElement: T): SortedSet<T> {
        if (root == null) return KtBinaryTree()
        return KtBinaryTree(lastOf(fromElement, root), fromElement, null)
    }

    override fun first(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left!!
        }
        return current.value
    }

    private fun firstOf(value: T, node: Node<T>?): Node<T> {
        var current: Node<T> = node ?: throw NoSuchElementException()
        while (value < current.value) {
            current = current.left ?: break
        }
        return current
    }

    override fun last(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.right != null) {
            current = current.right!!
        }
        return current.value
    }

    private fun lastOf(value: T, node: Node<T>?): Node<T> {
        var current: Node<T> = node ?: throw NoSuchElementException()
        while (value > current.value) {
            current = current.right ?: break
        }
        return current
    }

    private fun subOf(fromElement: T, toElement: T, node: Node<T>?): Node<T> {
        if (node == null) throw NoSuchElementException()
        return when {
            fromElement > node.value -> lastOf(fromElement, node)
            toElement < node.value -> firstOf(toElement, node)
            else -> return node
        }
    }

    private constructor(root: Node<T>, first: T?, last: T?) : this() {
        this.root = root
        this.first = first
        this.last = last
    }
}