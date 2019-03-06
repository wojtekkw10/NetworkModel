public class App {
    public static void main(String[] args)
    {
        //------------------------------------------------------
        //Lista 2 Zad 1 ----------------------------------------

        //------------------------------------------------------
        //Zad 1.1 --------------------------------------
        int monteCarloSamples = 1000;
        NetworkModel networkModel = new NetworkModel(20);
        networkModel.verbose = true;
        for(int i=1; i<20; i++){
            networkModel.setEdgeDurability(i, i+1, 0.95);
        }

        System.out.println("Zad 1.1: "+String.format( "%.5f", networkModel.getReliability(1, monteCarloSamples)));

        //-------------------------------------------------------
        //Zad 1.2 ---------------------------------------
        networkModel.setEdgeDurability(1,20, 0.95);
        System.out.println("Zad 1.2: "+String.format( "%.5f", networkModel.getReliability(1, monteCarloSamples)));

        //-------------------------------------------------------
        //Zad 1.3 ---------------------------------------
        networkModel.setEdgeDurability(1,10, 0.8);
        networkModel.setEdgeDurability(5,15, 0.7);
        System.out.println("Zad 1.3: "+String.format( "%.5f", networkModel.getReliability(1, monteCarloSamples)));

        //-------------------------------------------------------
        //Zad 1.4
        networkModel.setEdgeDurability(1,3, 0.95);
        networkModel.setEdgeDurability(3,7, 0.95);
        networkModel.setEdgeDurability(4,9, 0.95);
        networkModel.setEdgeDurability(7,10, 0.95);
        System.out.println("Zad 1.4: "+String.format( "%.5f", networkModel.getReliability(1, monteCarloSamples)));

        //------------------------------------------------------
        //Lista 2 Zad 2 ----------------------------------------

        //------------------------------------------------------
        //Zad 2.1 ----------------------------------------------
        double durability = 0.999;
        double capacity = 100;
        NetworkModel networkModel1 = new NetworkModel(10);
        networkModel1.verbose = true;
        for(int i=1; i<10; i++){
            networkModel1.setEdge(i, i+1, durability, capacity);
        }
        networkModel1.setEdge(1,10, durability, capacity);
        networkModel1.setEdge(1,5, durability, capacity);
        networkModel1.setEdge(2,8, durability, capacity);
        networkModel1.setEdge(2,4, durability, capacity);
        networkModel1.setEdge(3,9, durability, capacity);
        networkModel1.setEdge(3,6, durability, capacity);
        networkModel1.setEdge(4,10, durability, capacity);
        networkModel1.setEdge(4,5, durability, capacity);
        networkModel1.setEdge(5,7, durability, capacity);
        networkModel1.setEdge(5,9, durability, capacity);

        networkModel1.setAveragePacketSize(10);

        //TODO: capacity > flow * averagepacketSize w getDistances
        if(!networkModel1.setStream(1,10, 9)) System.out.println("Error");
        if(!networkModel1.setStream(2,6, 8)) System.out.println("Error");
        if(!networkModel1.setStream(7,8, 7)) System.out.println("Error");
        if(!networkModel1.setStream(3,4, 1)) System.out.println("Error");
        if(!networkModel1.setStream(4,10, 7)) System.out.println("Error");

        System.out.println("Zad 2.2: Average packet delay: "+networkModel1.getAveragePacketDelay());

    }
}

//TODO: refactor the NetworkModel class using CapacityGraph class
//TODO: add setStream function to CapacityGraph class