import javax.swing.plaf.synth.SynthTextAreaUI;
import java.lang.reflect.Array;
import java.security.SecureRandom;
import java.security.Security;
import java.util.ArrayList;

public class NetworkModel{
    public CapacityGraph network;

    private double averagePacketSizeSum = 0;
    private double averagePacketSizeWeight = 0;

    private int numOfVertices;
    public boolean verbose = false;

    //gdy true brany jest Sum
    boolean constantAveragePacketSize = true;

    public NetworkModel(int numOfVertices){
        this.numOfVertices = numOfVertices;
        network = new CapacityGraph(numOfVertices);
    }

    private double getEdge(ArrayList<ArrayList<Double>> matrix, int i, int j){
        return matrix.get(i-1).get(j-1-i);
    }

    public void setEdge(int i, int j, double durability, double capacity){
        setEdgeDurability(i,j,durability);
        setEdgeCapacity(i,j,capacity);
    }

    public void setEdgeDurability(int i, int j, double durability){
        network.setEdge(i,j,durability);
    }

    public void setEdgeCapacity(int i, int j, double capacity){
        network.setCapacity(i,j,capacity);

    }

    //gdy average packet size constant ostatni argument nie jest brany pod uwage
    public boolean setStream(int i, int j, double numOfPackets, int avgPacketSize){
        ArrayList<Integer> path;
        if(!constantAveragePacketSize) path = network.setStream(i,j,numOfPackets*avgPacketSize);
        else path = network.setStream(i,j,numOfPackets*getAveragePacketSize());
        if(path!=null) {
            if(!constantAveragePacketSize){
                averagePacketSizeSum+=avgPacketSize*numOfPackets;
                averagePacketSizeWeight += numOfPackets;
            }

            return true;
        }
        else return false;

    }

    //liczone od 1
    public boolean setStream(int i, int j, int numOfPackets){

        ArrayList<Integer> path = network.setStream(i,j,numOfPackets*getAveragePacketSize());
        if(path!=null) {
            return true;
        }
        else return false;

    }

    //liczone od 0
    public void deleteStream(int i, int j){
        network.deleteStream(i,j);
    }

    public void setAveragePacketSize(double avg){
        this.averagePacketSizeSum = avg;
    }

    boolean isConnected(Graph g){
        return g.isConnected();
    }
    boolean isConnected(){
        return network.isConnected();
    }


    public double getReliability(int timesteps, int monteCarloSamples){

        int reliabilityCount = 0;


        for(int n=0; n<monteCarloSamples; n++) {
            Graph temp = new Graph(numOfVertices);
            temp.edges = cloneArrayListDouble(network.edges);

            if(verbose && (n%(monteCarloSamples/10))==0) System.out.print(".");

            for (int k = 0; k < timesteps; k++) {
                simulateTimestep(temp);
            }
            //print(temp);
            if (isConnected(temp)) reliabilityCount++;
        }

        if(verbose) System.out.println("Done");
        return reliabilityCount/(double)monteCarloSamples;
    }

    public int getEdgeCount(){
        int counter = 0;
        for(int i=0; i<numOfVertices; i++){
            for(int j=0;j<numOfVertices; j++){
                if(network.edges.get(i).get(j)>0 && i<j) counter++;
            }
        }
        return counter;
    }

    private void simulateTimestep(Graph g){
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < numOfVertices; i++) {
            for (int j = 0; j < numOfVertices; j++) {
                if(g.edges.get(i).get(j)>0){
                    if (random.nextDouble() > g.edges.get(i).get(j) && i<j) {
                        //System.out.println("Usunieto: "+i+" "+j);
                        g.edges.get(i).set(j, 0.0);
                        g.edges.get(j).set(i, 0.0);
                    }
                }

            }
        }
    }

    private void simulateTimestep1(CapacityGraph g){
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < numOfVertices; i++) {
            for (int j = 0; j < numOfVertices; j++) {
                if(g.edges.get(i).get(j)>0){
                    if (random.nextDouble() > g.edges.get(i).get(j) && i<j) {
                        //System.out.println("Usunieto: "+i+" "+j);
                        g.edges.get(i).set(j, 0.0);
                        g.edges.get(j).set(i, 0.0);

                        g.recalculateStreamsForDamagedEdge(i+1, j+1);
                    }
                }

            }
        }
    }

    public double getAveragePacketSize(){
        if(!constantAveragePacketSize) return averagePacketSizeSum/averagePacketSizeWeight;
        else return averagePacketSizeSum;
    }

    public double getAveragePacketDelay(CapacityGraph g){
        double SUM_e = 0;
        for(int i=0; i<numOfVertices; i++) {
            for (int j = 0; j < numOfVertices; j++) {
                if (i < j && g.edges.get(i).get(j) > 0) {
                    double flow = g.flow.get(i).get(j);
                    double capacity = g.capacity.get(i).get(j);
                    SUM_e += flow / (capacity / getAveragePacketSize() - flow);
                }
            }
        }

        double G = 0;
        for(int i=0; i<numOfVertices; i++){
            for(int j=0; j<numOfVertices; j++){
                if(i<j) G+= g.flow.get(i).get(j);
            }
        }
        return 1/G * SUM_e;
    }
    public double getAveragePacketDelay(){
        return getAveragePacketDelay(this.network);
    }



        private void print(ArrayList<ArrayList<Double>> matrix){
        for(int i=0; i<matrix.size(); i++){
            for(int j=0; j<matrix.get(i).size(); j++){
                if(matrix.get(i).get(j)>0 && i<j) System.out.println((i+1)+" "+(j+1)+" "+matrix.get(i).get(j));
            }
        }
    }

    public void compare(ArrayList<ArrayList<Double>> arr){
        for(int i=0; i<numOfVertices; i++){
            for(int j=0; j<numOfVertices;j++){
                if(i<j){
                    if(!network.edges.get(i).get(j).equals(arr.get(i).get(j))) System.out.println("i: "+i+", j: "+j);

                }
            }
        }
    }

    ArrayList<ArrayList<Double>> cloneArrayListDouble(ArrayList<ArrayList<Double>> a){
        ArrayList<ArrayList<Double>> b = new ArrayList<>();
        for(int i=0; i<a.size(); i++){
            ArrayList<Double> row = new ArrayList<>();
            for(int j=0; j<a.get(i).size(); j++){
                row.add(a.get(i).get(j));
            }
            b.add(row);
        }
        return b;
    }
    ArrayList<ArrayList<Integer>> cloneArrayListInt2(ArrayList<ArrayList<Integer>> a){
        ArrayList<ArrayList<Integer>> b = new ArrayList<>();
        for(int i=0; i<a.size(); i++){
            ArrayList<Integer> row = new ArrayList<>();
            for(int j=0; j<a.get(i).size(); j++){
                row.add(a.get(i).get(j));
            }
            b.add(row);
        }
        return b;
    }
    ArrayList<ArrayList<ArrayList<Integer>>> cloneArrayListInt3(ArrayList<ArrayList<ArrayList<Integer>>> a){
        ArrayList<ArrayList<ArrayList<Integer>>> b = new ArrayList<>();
        for(int i=0; i<a.size(); i++){
            ArrayList<ArrayList<Integer>> array = new ArrayList<>();
            for(int j=0; j<a.get(i).size(); j++){
                ArrayList<Integer> row = new ArrayList<>();
                for(int k=0; k<a.get(i).get(j).size(); k++){
                    row.add(a.get(i).get(j).get(k));
                }
                array.add(row);
            }
            b.add(array);
        }
        return b;
    }

    private CapacityGraph cloneCapacityGraph(){

        CapacityGraph networkCopy = new CapacityGraph(numOfVertices);
        networkCopy.edges = cloneArrayListDouble(network.edges);
        networkCopy.flow = cloneArrayListDouble(network.flow);
        networkCopy.capacity = cloneArrayListDouble(network.capacity);
        networkCopy.numOfPacketsForStream = cloneArrayListInt2(network.numOfPacketsForStream);
        networkCopy.paths = cloneArrayListInt3(network.paths);
        return networkCopy;
    }

    public double getDelayReliability(double T_max, int timesteps, int monteCarloSamples) {
        int reliabilityCount = 0;
        int divider = monteCarloSamples;

        //copy network


        //loop montecarlo

        for(int n=0; n<monteCarloSamples; n++) {
            CapacityGraph networkCopy = cloneCapacityGraph();

            if(verbose && (n%(monteCarloSamples/10))==0) System.out.print(".");

            boolean wasConnected = true;
            for (int k = 0; k < timesteps; k++) {
                if (!isConnected(networkCopy)) {
                    wasConnected = false;
                    break;
                }
                else simulateTimestep1(networkCopy);
            }
            if(wasConnected) {
                double r = getAveragePacketDelay(networkCopy);
                if (r<T_max) reliabilityCount++;
            }
            else{
                divider--;
            }
        }


        if (verbose) System.out.println("Done");
        return reliabilityCount / (double) divider;
    }


}
