package lesson3

import java.util.*
import kotlin.NoSuchElementException

// Attention: comparable supported but comparator is not
class KtBinaryTree<T : Comparable<T>> : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    private var root: Node<T>? = null

    override var size = 0
        private set

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
        size++
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
        if (remove(element, root, null)) {
            size--
            return true
        }
        return false
    }

    private fun remove(element: T, node: Node<T>?, father: Node<T>?): Boolean {
        if (node == null) return false
        val compare = element.compareTo(node.value)
        when {
            compare > 0 -> remove(element, node.right, node)
            compare < 0 -> remove(element, node.left, node)
            compare == 0 -> {
                when {
                    node.left == null && node.right == null -> father.replaceChild(node, null)
                    node.right == null -> father.replaceChild(node, node.left)
                    node.left == null -> father.replaceChild(node, node.right)
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
                        father.replaceChild(node, rep)
                        if (change.first.right != null) change.second.replaceChild(change.first, change.first.right)
                        else change.second.replaceChild(change.first, null)
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

    private fun Node<T>?.replaceChild(node: Node<T>, newNode: Node<T>?) {
        when {
            this == null -> root = newNode
            this.left != null && this.left!!.value.compareTo(node.value) == 0 -> this.left = newNode
            else -> this.right = newNode
        }
    }

    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    private fun find(value: T): Node<T>? =
            root?.let { find(it, value) }

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
        TODO()
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    override fun headSet(toElement: T): SortedSet<T> {
        TODO()
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    override fun tailSet(fromElement: T): SortedSet<T> {
        TODO()
    }

    override fun first(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left!!
        }
        return current.value
    }

    override fun last(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.right != null) {
            current = current.right!!
        }
        return current.value
    }
}
