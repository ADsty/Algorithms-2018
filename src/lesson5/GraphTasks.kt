@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson5

import java.util.*

/**
 * Эйлеров цикл.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
 * Если в графе нет Эйлеровых циклов, вернуть пустой список.
 * Соседние дуги в списке-результате должны быть инцидентны друг другу,
 * а первая дуга в списке инцидентна последней.
 * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
 * Веса дуг никак не учитываются.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
 *
 * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
 * связного графа ровно по одному разу
 */
fun Graph.findEulerLoop(): List<Graph.Edge> {
    TODO()
}

/**
 * Минимальное остовное дерево.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему минимальное остовное дерево.
 * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
 * вернуть любое из них. Веса дуг не учитывать.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ:
 *
 *      G    H
 *      |    |
 * A -- B -- C -- D
 * |    |    |
 * E    F    I
 * |
 * J ------------ K
 */
fun Graph.minimumSpanningTree(): Graph {
    TODO()
}

/**
 * Максимальное независимое множество вершин в графе без циклов.
 * Сложная
 *
 * Дан граф без циклов (получатель), например
 *
 *      G -- H -- J
 *      |
 * A -- B -- D
 * |         |
 * C -- F    I
 * |
 * E
 *
 * Найти в нём самое большое независимое множество вершин и вернуть его.
 * Никакая пара вершин в независимом множестве не должна быть связана ребром.
 *
 * Если самых больших множеств несколько, приоритет имеет то из них,
 * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
 *
 * В данном случае ответ (A, E, F, D, G, J)
 *
 * Эта задача может быть зачтена за пятый и шестой урок одновременно
 * Сложность O(V*E) , Ресурсоемкость R(V^2)
 */
fun Graph.largestIndependentVertexSet(): Set<Graph.Vertex> {
    if (this.vertices.size == 0) return emptySet()
    val result = hashMapOf<Graph.Vertex, Set<Graph.Vertex>>()
    return largestIndependentVertexSet(result, this.vertices.first(), null)
}

fun Graph.largestIndependentVertexSet(result: MutableMap<Graph.Vertex, Set<Graph.Vertex>>,
                                      vertex: Graph.Vertex,
                                      parent: Graph.Vertex?): Set<Graph.Vertex> {
    return result.getOrPut(vertex) {
        val children = this.getNeighbors(vertex).filter { it != parent }
        val childrenSet = hashSetOf<Graph.Vertex>()
        val grandChildrenSet = hashSetOf<Graph.Vertex>()
        for (child in children) {
            childrenSet.addAll(this.largestIndependentVertexSet(result, child, vertex))
            for (grandChild in this.getNeighbors(child)) {
                if (grandChild != vertex) {
                    grandChildrenSet.addAll(this.largestIndependentVertexSet(result, grandChild, child))
                }
            }
        }
        if (childrenSet.size > grandChildrenSet.size + 1) childrenSet
        else grandChildrenSet + vertex
    }
}

/**
 * Наидлиннейший простой путь.
 * Сложная
 *
 * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
 * Простым считается путь, вершины в котором не повторяются.
 * Если таких путей несколько, вернуть любой из них.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ: A, E, J, K, D, C, H, G, B, F, I
 * Сложность O(V!) , Ресурсоемкость R(V!)
 */
fun Graph.longestSimplePath(): Path {
    if (this.vertices.isEmpty()) return Path()
    var best = Path(this.vertices.first())
    val queue = ArrayDeque<Path>()
    for (vertex in this.vertices) {
        queue.add(Path(vertex))
    }
    while (queue.isNotEmpty()) {
        val current = queue.pop()
        if (current.length > best.length) {
            best = current
            if (current.vertices.size == this.vertices.size) break
        }
        for (neighbor in this.getNeighbors(current.vertices.last())) {
            if (neighbor !in current) {
                queue.push(Path(current, this, neighbor))
            }
        }
    }
    return best
}