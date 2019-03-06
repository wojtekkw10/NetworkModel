import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.ArrayList;

public class NetworkModel {
    private CapacityGraph network;

    private double averagePacketSize;

    private int numOfVertices;
    public boolean verbose = false;

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

    public boolean setStream(int i, int j, double flow){
        ArrayList<Integer> path = network.setStream(i,j,flow);
        if(path!=null) {
            return true;
        }
        else return false;

    }

    void setAveragePacketSize(double avg){
        averagePacketSize = avg;
    }

    boolean isConnected(Graph g){
        return g.isConnected();
    }
    boolean isConnected(){
        return network.isConnected();
    }


    public double getReliability(int timesteps, int marcoPoloSamples){

        int reliabilityCount = 0;


        for(int n=0; n<marcoPoloSamples; n++) {
            Graph temp = new Graph(numOfVertices);
            temp.edges = network.getAllEdges();

            if(verbose && (n%(marcoPoloSamples/10))==0) System.out.print(".");
            for (int k = 0; k < timesteps; k++) {
                simulateTimestep(temp);
            }
            //print(temp);
            if (isConnected(temp)) reliabilityCount++;
        }

        if(verbose) System.out.println("Done");
        return reliabilityCount/(double)marcoPoloSamples;
    }

    private void simulateTimestep(Graph g){
        for (int i = 0; i < numOfVertices; i++) {
            for (int j = 0; j < numOfVertices; j++) {
                if (Math.random() > g.edges.get(i).get(j) && i<j) {
                    g.edges.get(i).set(j, 0.0);
                    g.edges.get(j).set(i, 0.0);
                }
            }
        }
    }

    public double getAveragePacketDelay(){
        double SUM_e = 0;
        for(int i=0; i<numOfVertices; i++){
            for(int j=0; j<numOfVertices; j++){
                if(i<j && network.edges.get(i).get(j)>0){
                    SUM_e+= network.flow.get(i).get(j)/
                            (network.capacity.get(i).get(j)/averagePacketSize - network.flow.get(i).get(j));
                }
            }
        }

        double G = 0;
        for(int i=0; i<numOfVertices; i++){
            for(int j=0; j<numOfVertices; j++){
                if(i<j) G+= network.flow.get(i).get(j);
            }
        }
        return 1/G * SUM_e;
    }

    private void print(ArrayList<ArrayList<Double>> matrix){
        for(int i=0; i<matrix.size(); i++){
            for(int j=0; j<matrix.get(i).size(); j++){
                if(matrix.get(i).get(j)>0 && i<j) System.out.println((i+1)+" "+(j+1)+" "+matrix.get(i).get(j));
            }
        }
    }
}
