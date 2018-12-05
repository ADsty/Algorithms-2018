package lesson5

import lesson5.impl.GraphBuilder
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class AbstractGraphTests {

    private fun Graph.Edge.isNeighbour(other: Graph.Edge): Boolean {
        return begin == other.begin || end == other.end || begin == other.end || end == other.begin
    }

    private fun List<Graph.Edge>.assert(shouldExist: Boolean, graph: Graph) {
        val edges = graph.edges
        if (shouldExist) {
            assertEquals(edges.size, size, "Euler loop should traverse all edges")
        } else {
            assertTrue(isEmpty(), "Euler loop should not exist")
        }
        for (edge in this) {
            assertTrue(edge in edges, "Edge $edge is not inside graph")
        }
        for (i in 0 until size - 1) {
            assertTrue(this[i].isNeighbour(this[i + 1]), "Edges ${this[i]} & ${this[i + 1]} are not incident")
        }
        assertTrue(this[0].isNeighbour(this[size - 1]), "Edges ${this[0]} & ${this[size - 1]} are not incident")
    }

    fun findEulerLoop(findEulerLoop: Graph.() -> List<Graph.Edge>) {
        val graph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(a, c)
        }.build()
        val loop = graph.findEulerLoop()
        loop.assert(true, graph)
        val graph2 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            val k = addVertex("K")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(c, d)
            addConnection(a, e)
            addConnection(d, k)
            addConnection(e, j)
            addConnection(j, k)
            addConnection(b, f)
            addConnection(c, i)
            addConnection(f, i)
            addConnection(b, g)
            addConnection(g, h)
            addConnection(h, c)
        }.build()
        val loop2 = graph2.findEulerLoop()
        loop2.assert(true, graph2)
    }

    fun minimumSpanningTree(minimumSpanningTree: Graph.() -> Graph) {
        val graph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(a, c)
        }.build()
        val tree = graph.minimumSpanningTree()
        assertEquals(2, tree.edges.size)
        assertEquals(2, tree.findBridges().size)
        val graph2 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            val k = addVertex("K")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(c, d)
            addConnection(a, e)
            addConnection(d, k)
            addConnection(e, j)
            addConnection(j, k)
            addConnection(b, f)
            addConnection(c, i)
            addConnection(f, i)
            addConnection(b, g)
            addConnection(g, h)
            addConnection(h, c)
        }.build()
        val tree2 = graph2.minimumSpanningTree()
        assertEquals(10, tree2.edges.size)
        assertEquals(10, tree2.findBridges().size)
    }

    fun largestIndependentVertexSet(largestIndependentVertexSet: Graph.() -> Set<Graph.Vertex>) {
        val graph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            addConnection(a, b)
            addConnection(a, c)
            addConnection(b, d)
            addConnection(c, e)
            addConnection(c, f)
            addConnection(b, g)
            addConnection(d, i)
            addConnection(g, h)
            addConnection(h, j)
        }.build()
        val independent = graph.largestIndependentVertexSet()
        assertEquals(setOf(graph["A"], graph["D"], graph["E"], graph["F"], graph["G"], graph["J"]),
                independent)

        val graph2 = GraphBuilder().apply {}.build()
        val independent2 = graph2.largestIndependentVertexSet()
        assertEquals(emptySet(), independent2)

        val graph3 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            val k = addVertex("K")
            val o = addVertex("O")
            val p = addVertex("P")
            val r = addVertex("R")
            val s = addVertex("S")
            val l = addVertex("L")
            val m = addVertex("M")
            val n = addVertex("N")
            val t = addVertex("T")
            val y = addVertex("Y")
            val z = addVertex("Z")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(c, d)
            addConnection(c, f)
            addConnection(f, g)
            addConnection(f, h)
            addConnection(h, i)
            addConnection(i, j)
            addConnection(j, k)
            addConnection(k, o)
            addConnection(o, p)
            addConnection(p, r)
            addConnection(s, l)
            addConnection(l, m)
            addConnection(m, n)
            addConnection(g, z)
            addConnection(z, e)
            addConnection(z, t)
            addConnection(t, s)
            addConnection(n, y)
        }.build()
        val independent3 = graph3.largestIndependentVertexSet()
        assertEquals(setOf(graph3["R"], graph3["C"], graph3["T"], graph3["E"], graph3["G"], graph3["H"], graph3["J"],
                graph3["L"], graph3["N"], graph3["O"], graph3["A"]), independent3)
    }

    fun longestSimplePath(longestSimplePath: Graph.() -> Path) {
        val graph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(a, c)
        }.build()
        val longestPath = graph.longestSimplePath()
        assertEquals(2, longestPath.length)

        val graph2 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            val k = addVertex("K")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(c, d)
            addConnection(a, e)
            addConnection(d, k)
            addConnection(e, j)
            addConnection(j, k)
            addConnection(b, f)
            addConnection(c, i)
            addConnection(f, i)
            addConnection(b, g)
            addConnection(g, h)
            addConnection(h, c)
        }.build()
        val longestPath2 = graph2.longestSimplePath()
        assertEquals(10, longestPath2.length)

        val graph3 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            val k = addVertex("K")
            val o = addVertex("O")
            val p = addVertex("P")
            val r = addVertex("R")
            val s = addVertex("S")
            val l = addVertex("L")
            val m = addVertex("M")
            val n = addVertex("N")
            val t = addVertex("T")
            val y = addVertex("Y")
            val z = addVertex("Z")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(c, d)
            addConnection(c, f)
            addConnection(f, g)
            addConnection(f, h)
            addConnection(h, i)
            addConnection(i, j)
            addConnection(j, k)
            addConnection(k, o)
            addConnection(o, p)
            addConnection(p, r)
            addConnection(r, s)
            addConnection(s, l)
            addConnection(l, m)
            addConnection(m, n)
            addConnection(g, z)
            addConnection(z, e)
            addConnection(z, t)
            addConnection(t, s)
            addConnection(n, y)
        }.build()
        val longestPath3 = graph3.longestSimplePath()
        assertEquals(15, longestPath3.length)

        val graph4 = GraphBuilder().apply {}.build()
        val longestPath4 = graph4.longestSimplePath()
        assertEquals(0, longestPath4.length)
    }

}