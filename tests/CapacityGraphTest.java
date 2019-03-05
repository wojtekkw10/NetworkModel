import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class CapacityGraphTest {
    @Test
    void getShortestPath(){
        CapacityGraph g = new CapacityGraph(4);
        g.setEdge(1,4,1);
        g.setEdge(1,2,1);
        g.setEdge(2,3,1);
        g.setEdge(3,4,1);
        g.setCapacity(1,4,10);
        g.setCapacity(1,2,5);
        g.setCapacity(2,3,5);
        g.setCapacity(3,4,5);

        ArrayList<Integer> path1 = new ArrayList<>();
        path1.add(1);
        path1.add(4);

        ArrayList<Integer> path2 = new ArrayList<>();
        path2.add(1);
        path2.add(2);
        path2.add(3);
        path2.add(4);


        assertEquals(path1, g.getShortestPath(1,4, 8));
        assertEquals(path2, g.getShortestPath(1,4, 4));
        assertNull(g.getShortestPath(1,4, 4));
    }
}
