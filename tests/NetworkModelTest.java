import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NetworkModelTest {

    @Test
    void isConnectedTest1() {
        NetworkModel NM = new NetworkModel(5);
        NM.setEdgeDurability(1,2,1);
        NM.setEdgeDurability(2,3,1);
        NM.setEdgeDurability(3,4,1);
        NM.setEdgeDurability(4,5,1);
        assertTrue(NM.isConnected());
    }
    @Test
    void isConnectedTest2() {
        NetworkModel NM = new NetworkModel(5);
        NM.setEdgeDurability(1,2,1);
        NM.setEdgeDurability(2,3,1);
        NM.setEdgeDurability(4,5,1);
        assertFalse(NM.isConnected());
    }
    @Test
    void isConnectedTest3() {
        NetworkModel NM = new NetworkModel(5);
        NM.setEdgeDurability(1,3,1);
        NM.setEdgeDurability(2,3,1);
        NM.setEdgeDurability(3,4,1);
        NM.setEdgeDurability(4,5,1);
        assertTrue(NM.isConnected());
    }
    @Test
    void isConnectedTest4() {
        NetworkModel NM = new NetworkModel(10);
        NM.setEdgeDurability(1,10,1);
        NM.setEdgeDurability(2,3,1);
        NM.setEdgeDurability(3,4,1);
        NM.setEdgeDurability(4,5,1);
        NM.setEdgeDurability(5,6,1);
        NM.setEdgeDurability(6,7,1);
        NM.setEdgeDurability(7,8,1);
        NM.setEdgeDurability(8,9,1);
        NM.setEdgeDurability(9,10,1);
        assertTrue(NM.isConnected());
    }
}