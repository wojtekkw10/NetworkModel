import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {
    @Test
    void setEdge() {
        Graph g = new Graph(2);
        g.setEdge(1,2,0.5);

        ArrayList<ArrayList<Double>> edges = g.getAllEdges();

        assertEquals(g.getEdge(1,2), 0.5);
        assertEquals(edges.get(0).get(1), 0.5);
        assertEquals(edges.get(1).get(0), 0.5);
    }
    @Test
    void isConnected() {
        Graph g = new Graph(4);
        g.setEdge(1,3,1);
        g.setEdge(2,3,1);
        g.setEdge(3,4,1);
        assertTrue(g.isConnected());

        Graph g1 = new Graph(4);
        g1.setEdge(1,4,1);
        g1.setEdge(2,3,1);
        g1.setEdge(3,4,1);
        assertTrue(g1.isConnected());

        Graph g2 = new Graph(4);
        g2.setEdge(1,2,1);
        g2.setEdge(1,4,1);
        g2.setEdge(2,4,1);
        assertFalse(g2.isConnected());
    }
    @Test
    void print() {
        Graph g = new Graph(4);
        g.setEdge(1,3,1);
        g.setEdge(2,3,1);
        g.setEdge(3,4,1);
        assertEquals("(1,3)(2,3)(3,4)", g.print());

        Graph g2 = new Graph(4);
        g2.setEdge(1,2,1);
        g2.setEdge(1,4,1);
        g2.setEdge(2,4,1);
        assertEquals("(1,2)(1,4)(2,4)", g2.print());
    }
    @Test
    void isPathValid() {
        Graph g = new Graph(4);
        g.setEdge(1,3,1);
        g.setEdge(2,3,1);
        g.setEdge(3,4,1);
        ArrayList<Integer> path = new ArrayList<>();
        path.add(1);
        path.add(3);
        path.add(4);
        assertTrue(g.isPathValid(path));

        Graph g2 = new Graph(4);
        g2.setEdge(1,4,1);
        g2.setEdge(2,3,1);
        g2.setEdge(3,4,1);
        ArrayList<Integer> path1 = new ArrayList<>();
        path1.add(1);
        path1.add(4);
        path1.add(3);
        path1.add(2);
        assertTrue(g2.isPathValid(path1));

        Graph g3 = new Graph(4);
        g3.setEdge(1,4,1);
        g3.setEdge(1,2,1);
        g3.setEdge(2,3,1);
        g3.setEdge(3,4,1);
        ArrayList<Integer> path2 = new ArrayList<>();
        path2.add(1);
        path2.add(4);
        path2.add(2);
        path2.add(3);
        assertFalse(g3.isPathValid(path2));
    }

    @Test
    void getAdjacentVertices() {
        Graph g = new Graph(5);
        g.setEdge(2,3,1);
        g.setEdge(3,4,1);
        g.setEdge(4,5,1);
        ArrayList<Integer> path = new ArrayList<>();
        path.add(3);
        path.add(5);
        assertEquals(path, g.getAdjacentVertices(4));


        Graph g1 = new Graph(4);
        g1.setEdge(1,4,1);
        g1.setEdge(2,3,1);
        g1.setEdge(3,4,1);
        ArrayList<Integer> adj = new ArrayList<>();
        adj.add(4);
        assertEquals(adj, g1.getAdjacentVertices(1));
    }
    @Test
    void getIndexOfMin() {
        Graph g = new Graph(5);
        ArrayList<Double> array = new ArrayList<>();
        array.add(4.0);
        array.add(5.0);
        array.add(3.0);
        array.add(10.0);
        array.add(1.0);
        ArrayList<Boolean> mask = new ArrayList<>();
        mask.add(false);
        mask.add(false);
        mask.add(false);
        mask.add(false);
        mask.add(true);

        assertEquals(2, g.getIndexOfMinMasked(array, mask));
    }

    @Test
    void getDistancesFromVertex() {
        Graph g = new Graph(4);
        g.setEdge(1,3,1);
        g.setEdge(2,3,1);
        g.setEdge(3,4,1);
        ArrayList<Double> distance = new ArrayList<>();
        distance.add(0.0);
        distance.add(2.0);
        distance.add(1.0);
        distance.add(2.0);
        assertEquals(distance, g.getDistancesFromVertex(1, false));

        Graph g2 = new Graph(4);
        g2.setEdge(1,4,1);
        g2.setEdge(2,3,1);
        g2.setEdge(3,4,1);
        ArrayList<Double> path1 = new ArrayList<>();
        path1.add(0.0);
        path1.add(3.0);
        path1.add(2.0);
        path1.add(1.0);
        assertEquals(path1, g2.getDistancesFromVertex(1, false));

        Graph g3 = new Graph(4);
        g3.setEdge(1,2,100);
        g3.setEdge(1,4,1);
        g3.setEdge(2,3,1);
        g3.setEdge(3,4,1);
        ArrayList<Double> path2 = new ArrayList<>();
        path2.add(0.0);
        path2.add(3.0);
        path2.add(2.0);
        path2.add(1.0);
        assertEquals(path2, g3.getDistancesFromVertex(1, false));
    }
    @Test
    void getShortestPath(){
        Graph g = new Graph(4);
        g.setEdge(1,3,1);
        g.setEdge(2,3,1);
        g.setEdge(3,4,1);
        ArrayList<Integer> path = new ArrayList<>();
        path.add(1);
        path.add(3);
        path.add(4);
        assertEquals(path, g.getShortestPath(1,4));

        Graph g2 = new Graph(4);
        g2.setEdge(1,4,1);
        g2.setEdge(2,3,1);
        g2.setEdge(3,4,1);
        ArrayList<Double> path1 = new ArrayList<>();
        path1.add(0.0);
        path1.add(3.0);
        path1.add(2.0);
        path1.add(1.0);
        assertEquals(path1, g2.getDistancesFromVertex(1, false));

        Graph g3 = new Graph(4);
        g3.setEdge(1,2,100);
        g3.setEdge(1,4,1);
        g3.setEdge(2,3,1);
        g3.setEdge(3,4,1);
        ArrayList<Double> path2 = new ArrayList<>();
        path2.add(0.0);
        path2.add(3.0);
        path2.add(2.0);
        path2.add(1.0);
        assertEquals(path2, g3.getDistancesFromVertex(1, false));
    }
}
