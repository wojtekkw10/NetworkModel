public class App {
    public static void main(String[] args)
    {
        NetworkModel networkModel = new NetworkModel(20);
        networkModel.verbose = true;
        for(int i=1; i<20; i++){
            networkModel.setEdgeDurability(i, i+1, 0.95);
        }
        networkModel.setEdgeDurability(1,20, 0.95);
        networkModel.setEdgeDurability(1,10, 0.8);
        networkModel.setEdgeDurability(5,15, 0.7);

        System.out.println(String.format( "%.5f", networkModel.getReliability(1, 1000000)));

        //zad2
        double p = 0.999;
        NetworkModel networkModel1 = new NetworkModel(10);
        networkModel1.verbose = true;
        for(int i=1; i<10; i++){
            networkModel1.setEdgeDurability(i, i+1, p);
        }
        networkModel1.setEdgeDurability(1,10, p);
        networkModel1.setEdgeDurability(1,5, p);
        networkModel1.setEdgeDurability(2,8, p);
        networkModel1.setEdgeDurability(2,4, p);
        networkModel1.setEdgeDurability(3,9, p);
        networkModel1.setEdgeDurability(3,6, p);
        networkModel1.setEdgeDurability(4,10, p);
        networkModel1.setEdgeDurability(4,5, p);
        networkModel1.setEdgeDurability(5,7, p);
        networkModel1.setEdgeDurability(5,9, p);
        System.out.println(String.format( "%.5f", networkModel1.getReliability(1, 1000000)));

    }
}

//TODO: refactor the NetworkModel class using CapacityGraph class
//TODO: add setStream function to CapacityGraph class