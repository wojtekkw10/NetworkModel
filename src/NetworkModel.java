import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.ArrayList;

public class NetworkModel {
    private ArrayList<ArrayList<Double>> E = new ArrayList<>();

    //capacity > flow
    private ArrayList<ArrayList<Double>> capacityMatrix = new ArrayList<>(); //c(e)
    private ArrayList<ArrayList<Double>> flowMatrix = new ArrayList<>();     //a(c)
    private ArrayList<ArrayList<Double>> physicalFlowMatrix = new ArrayList<>();
    private double averagePacketSize;

    private int numOfVertices;
    public boolean verbose = false;

    public NetworkModel(int numOfVertices){
        this.numOfVertices = numOfVertices;
        for(int i=0; i<numOfVertices; i++) {
            ArrayList<Double> row = new ArrayList<>();
            ArrayList<Double> row1 = new ArrayList<>();
            ArrayList<Double> row2 = new ArrayList<>();

            for(int j=0;j<numOfVertices-i-1; j++){
                row.add(0.0);
                row1.add(0.0);
                row2.add(0.0);
            }
            E.add(row);
            capacityMatrix.add(row1);
            flowMatrix.add(row2);
        }
    }
    public void setEdge(ArrayList<ArrayList<Double>> matrix, int i, int j, double value){
        if(i<j) matrix.get(i-1).set(j-1-i, value);
        else if(i>j) matrix.get(j-1).set(i-1-j, value);
    }
    private double getEdge(ArrayList<ArrayList<Double>> matrix, int i, int j){
        return matrix.get(i-1).get(j-1-i);
    }

    public void setEdgeDurability(int i, int j, double durability){
        setEdge(E, i, j, durability);
    }

    public void setEdgeCapacity(int i, int j, double capacity){
        setEdge(capacityMatrix, i, j, capacity);

    }

    public void setEdgeFlow(int i, int j, double flow){
        setEdge(flowMatrix, i, j, flow);

    }

    private boolean isConnected(ArrayList<ArrayList<Double>> E){
        ArrayList<Boolean> V = new ArrayList<>();
        for(int i=0; i<numOfVertices; i++) V.add(false);

        V.set(0,true);
        for(int k=0; k<numOfVertices; k++){
            for(int i=0; i<E.size(); i++) {
                for(int j=0; j<E.get(i).size(); j++){
                    if(V.get(i) && E.get(i).get(j)>0 ) V.set(i+j+1, true);
                    if(V.get(i+j+1) && E.get(i).get(j)>0) V.set(i, true);
                }
            }
        }

        boolean isConnected = true;
        for(int i=0; i<numOfVertices; i++){
            if(!V.get(i)) isConnected = false;
        }
        return isConnected;
    }

    //overloaded
    boolean isConnected(){
        return isConnected(E);
    }

    public double getReliability(int timesteps, int marcoPoloSamples){

        int reliabilityCount = 0;


        for(int n=0; n<marcoPoloSamples; n++) {
            ArrayList<ArrayList<Double>> temp = (ArrayList<ArrayList<Double>>) E.clone();
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

    private void simulateTimestep(ArrayList<ArrayList<Double>> durabilityMatrix){
        for (int i = 0; i < E.size(); i++) {
            for (int j = 0; j < E.get(i).size(); j++) {
                if (Math.random() > durabilityMatrix.get(i).get(j)) {
                    durabilityMatrix.get(i).set(j, 0.0);
                }
            }
        }
    }

    public double averagePacketDelay(){
        double SUM_e = 0;
        for(int i=0; i<E.size(); i++){
            for(int j=0; j<E.get(i).size(); j++){
                SUM_e+= flowMatrix.get(i).get(j)/
                        (capacityMatrix.get(i).get(j)/averagePacketSize - flowMatrix.get(i).get(j));
            }
        }

        double G = 0;
        for(int i=0; i<E.size(); i++){
            for(int j=0; j<E.get(i).size(); j++){
                if(i<j) G+= flowMatrix.get(i).get(j);
            }
        }
        return 1/G * SUM_e;
    }

    private void print(ArrayList<ArrayList<Double>> matrix){
        for(int i=0; i<matrix.size(); i++){
            for(int j=0; j<matrix.get(i).size(); j++){
                if(matrix.get(i).get(j)>0) System.out.println((i+1)+" "+(j+i+2)+" "+matrix.get(i).get(j));
            }
        }
    }

    public void findFlowPaths(){
        for(int i=0; i<flowMatrix.size(); i++) {
            for (int j = 0; j < flowMatrix.get(i).size(); j++) {
                if (flowMatrix.get(i).get(j) > 0) {
                    ArrayList<ArrayList<Integer>> paths = new ArrayList<>();
                    ArrayList<Double> pathWeigths = new ArrayList<>();

                    System.out.print("\nCurrent: ");
                    System.out.println((i+1)+" "+(j+2+i));
                    System.out.println("Print");
                    print(flowMatrix);
                    ArrayList<Integer> pathMatrix = new ArrayList<>();
                    for (int z = 0; z < numOfVertices; z++) pathMatrix.add(0);
                    double flow = flowMatrix.get(i).get(j);

                    pathMatrix.set(i, 1);
                    ArrayList<Integer> path = new ArrayList<>();
                    path.add(i);
                    paths.add(path);
                    pathWeigths.add(0.0);

                    int count = 1;
                    int searchCount = 1;

                    for (int k = 0; k < numOfVertices*numOfVertices; k++) {
                        for (int n = 0; n < flowMatrix.size(); n++) {
                            for (int m = 0; m < flowMatrix.get(n).size(); m++) {
                                if (pathMatrix.get(n) == searchCount && pathMatrix.get(n+m+1)==0) {
                                    count++;
                                    if(pathMatrix.get(n+m+1)==0) pathMatrix.set(n + m + 1, count);
                                    for(int q=0; q<paths.size(); q++){
                                        if(paths.get(q).get(paths.get(q).size()-1)==n){
                                            ArrayList<Integer> newPath = (ArrayList<Integer>)paths.get(q).clone();
                                            newPath.add(n+m+1);
                                            paths.add(newPath);

                                            Double w = pathWeigths.get(q) + flowMatrix.get(n).get(m);
                                            pathWeigths.add(w);

                                            //scalanie
                                            for(int x=0; x<paths.size(); x++){
                                                for(int y =0; y<paths.size(); y++){
                                                    if(paths.get(x).get(paths.get(x).size()-1).equals(paths.get(y).get(paths.get(y).size()-1))){
                                                        if(pathWeigths.get(x) < pathWeigths.get(y)){
                                                            paths.remove(y);
                                                            pathWeigths.remove(y);
                                                            count--;
                                                        }
                                                        else if(pathWeigths.get(x) > pathWeigths.get(y)){
                                                            paths.remove(x);
                                                            pathWeigths.remove(x);
                                                            count--;
                                                        }
                                                    }
                                                }
                                            }

                                            break;
                                        }

                                    }

                                }
                                if (pathMatrix.get(n+m+1) == searchCount && pathMatrix.get(n)==0 && E.get(n).get(m) > 0) {
                                    count++;
                                    pathMatrix.set(n, count);
                                    paths.get(0).add(n);
                                    for(int q=0; q<paths.size(); q++){
                                        if(paths.get(q).get(paths.get(q).size()-1)==n+m+1){
                                            ArrayList<Integer> newPath = (ArrayList<Integer>)paths.get(q).clone();
                                            newPath.add(n);
                                            paths.add(newPath);

                                            Double w = pathWeigths.get(q) + flowMatrix.get(n).get(m);
                                            pathWeigths.add(w);
                                            break;
                                        }
                                    }
                                }
                            }

                        }
                        searchCount++;
                    }
                    for(int x=0; x<paths.size(); x++){
                        System.out.println("Total flow: "+pathWeigths.get(x));

                        for(int y=0; y<paths.get(x).size(); y++){
                            System.out.print((paths.get(x).get(y)+1)+", ");
                        }
                        System.out.println("");
                    }
                }
            }
        }
    }
}
