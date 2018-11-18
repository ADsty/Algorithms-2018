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
            try {
                for (node in this) {
                    result++
                }
            } catch (ex: NoSuchElementException) {

            }
            return result
        }

    private class Node<T>(val value: T) {
        var parent: Node<T>? = null

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
        newNode.parent = closest
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
     * Сложность O(h), Ресурсоемкость R(1) , где h - глубина дерева
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
                val nodeLeft = node.left
                val nodeRight = node.right
                when {
                    nodeLeft == null && nodeRight == null -> replaceChild(father, node, null)
                    nodeRight == null -> replaceChild(father, node, nodeLeft)
                    nodeLeft == null -> replaceChild(father, node, nodeRight)
                    else -> {
                        val change = minimum(nodeRight, node)
                        val rep = Node(change.first.value)
                        if (rep.value != nodeLeft.value) rep.left = nodeLeft
                        else rep.left = null
                        when {
                            rep.value != nodeRight.value -> rep.right = nodeRight
                            nodeRight.right != null -> rep.right = nodeRight.right
                            else -> rep.right = null
                        }
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
        return if (node.left == null) Pair(node, parent)
        else return minimum(node.left!!, node)
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
         * Сложность O(h) , Ресурсоемкость R(n) , где h - глубина дерева , n - количество элементов в дереве,
         * которые будут записаны в очередь queue
         */
        private fun findNext(): Node<T>? {
            if (queue.peekFirst() == null) return null
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
            when {
                first != null && last != null && (first!! > current!!.value || last!! < current!!.value) -> {
                    while (current != null &&
                            first != null && last != null && (first!! > current!!.value || last!! < current!!.value)) {
                        current = findNext()
                        if (current == null) break
                    }
                }
                first == null && last != null && last!! <= current!!.value -> {
                    while (current != null && first == null && last != null && last!! <= current!!.value) {
                        current = findNext()
                        if (current == null) break
                    }
                }
                last == null && first != null && first!! > current!!.value -> {
                    while (current != null && last == null && first != null && first!! > current!!.value) {
                        current = findNext()
                        if (current == null) break
                    }
                }
            }
            return (current ?: throw NoSuchElementException()).value
        }

        /**
         * Удаление следующего элемента
         * Сложная
         * Сложность O(1) , Ресурсоемкость R(1)
         */
        override fun remove() {
            if (current == null) return
            else remove(current!!.value, current, current!!.parent)
        }
    }

    override fun iterator(): MutableIterator<T> = BinaryTreeIterator()

    override fun comparator(): Comparator<in T>? = null

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     * Сложность O(a) , Ресурсоемкость R(1) , где a - глубина на которой находится первый элемент
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        if (root == null) return KtBinaryTree()
        return KtBinaryTree(rootOf(fromElement, toElement, root), fromElement, toElement)
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     * Сложность O(a) , Ресурсоемкость R(1) , где a - глубина на которой находится первый элемент
     */
    override fun headSet(toElement: T): SortedSet<T> {
        if (root == null) return KtBinaryTree()
        return KtBinaryTree(firstOfLeft(toElement, root), null, toElement)
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     * Сложность O(a) , Ресурсоемкость R(1) , где a - глубина на которой находится первый элемент
     */
    override fun tailSet(fromElement: T): SortedSet<T> {
        if (root == null) return KtBinaryTree()
        return KtBinaryTree(firstOfRight(fromElement, root), fromElement, null)
    }

    override fun first(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left!!
        }
        return current.value
    }

    private fun firstOfLeft(value: T, node: Node<T>?): Node<T> {
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

    private fun firstOfRight(value: T, node: Node<T>?): Node<T> {
        var current: Node<T> = node ?: throw NoSuchElementException()
        while (value > current.value) {
            current = current.right ?: break
        }
        return current
    }

    private fun rootOf(fromElement: T, toElement: T, node: Node<T>?): Node<T> {
        if (node == null) throw NoSuchElementException()
        return when {
            fromElement > node.value -> firstOfRight(fromElement, node)
            toElement < node.value -> firstOfLeft(toElement, node)
            else -> return node
        }
    }

    private constructor(node: Node<T>, first: T?, last: T?) : this() {
        root = node
        this.first = first
        this.last = last
    }
}