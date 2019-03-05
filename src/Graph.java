import java.util.ArrayList;

public class Graph {
    protected ArrayList<ArrayList<Double>> edges = new ArrayList<>();
    protected int numOfVertices;
    public Graph(){}
    public Graph(int numOfVertices){
        this.numOfVertices = numOfVertices;
        for(int i=0; i<numOfVertices; i++) {
            ArrayList<Double> row = new ArrayList<>();

            for(int j=0;j<numOfVertices; j++){
                row.add(0.0);
            }
            edges.add(row);
        }
    }
    public double getEdge(int i, int j){
        return edges.get(i-1).get(j-1);
    }
    public void setEdge(int i, int j, double weight){
        edges.get(i-1).set(j-1, weight);
        edges.get(j-1).set(i-1, weight);
    }
    public ArrayList<ArrayList<Double>> getAllEdges(){
        return edges;
    }

    public boolean isConnected(){
        ArrayList<Boolean> V = new ArrayList<>();
        for(int i=0; i<numOfVertices; i++) V.add(false);

        V.set(0,true);
        for(int k=0; k<=numOfVertices; k++){
            for(int i=1; i<=numOfVertices; i++) {
                for(int j=1; j<=numOfVertices; j++){
                    if(V.get(i-1) && getEdge(i, j)>0) V.set(j-1, true);
                    if(V.get(j-1) && getEdge(i, j)>0) V.set(i-1, true);
                }
            }
        }

        boolean isConnected = true;
        for(int i=0; i<numOfVertices; i++){
            if(!V.get(i)) isConnected = false;
        }
        return isConnected;
    }
    public String print(){
        StringBuilder s = new StringBuilder();
        for(int i=1; i<=numOfVertices; i++){
            for(int j=0; j<=numOfVertices; j++){
                if(i<j && getEdge(i, j)>0) s.append("("+i+","+j+")");
            }
        }
        return s.toString();
    }
    public boolean isPathValid(ArrayList<Integer> vertices){
        for(int i=0; i<vertices.size()-1; i++){
            int currentV = vertices.get(i);
            int nextV = vertices.get(i+1);
            if(!(getEdge(currentV, nextV)>0)) return false;
        }
        return true;
    }

    public ArrayList<Integer> getAdjacentVertices(int v){
        ArrayList<Integer> vert = new ArrayList<>();
        for(int i=1; i<=numOfVertices; i++){
            if(getEdge(v,i)>0) vert.add(i);
        }
        return vert;
    }
    public int getIndexOfMinMasked(ArrayList<Double> array, ArrayList<Boolean> mask){
        int minIndex = array.size();
        double minValue = Double.MAX_VALUE;

        for(int i=0; i<array.size(); i++){
            if(minValue>array.get(i) && !mask.get(i)){
                minIndex = i;
                minValue = array.get(i);
            }
        }
        return minIndex;
    }

    void initializeArray(ArrayList<Integer> array, int size, int value){
        for(int i=0; i<size; i++){
            array.add(value);
        }
    }
    void initializeArray(ArrayList<Double> array, int size, double value){
        array.clear();
        for(int i=0; i<size; i++){
            array.add(value);
        }
    }
    void initializeArray(ArrayList<Boolean> array, int size, boolean value){
        array.clear();
        for(int i=0; i<size; i++){
            array.add(value);
        }
    }

    public ArrayList<Double> getDistancesFromVertex(int x, boolean path){
        x--;
        ArrayList<Double> prevVertex = new ArrayList<>();
        ArrayList<Double> shortestDistance = new ArrayList<>();
        ArrayList<Boolean> visited = new ArrayList<>();

        initializeArray(prevVertex, numOfVertices, 0);
        initializeArray(shortestDistance, numOfVertices, Double.MAX_VALUE);
        initializeArray(visited, numOfVertices, false);

        shortestDistance.set(x, 0.0);

        for(int i=0; i<numOfVertices; i++){
            int elem = getIndexOfMinMasked(shortestDistance, visited);
            ArrayList<Integer> adjVert = getAdjacentVertices(elem+1);
            for(int j=0; j<adjVert.size(); j++){
                if(shortestDistance.get(adjVert.get(j)-1)>shortestDistance.get(elem)+getEdge(elem+1, adjVert.get(j))){
                    shortestDistance.set(adjVert.get(j)-1, shortestDistance.get(elem)+getEdge(elem+1, adjVert.get(j)));
                    prevVertex.set(adjVert.get(j)-1, (double)elem);
                    visited.set(elem, true);
                }
            }
        }
        if(path) return prevVertex;
        else return shortestDistance;
    }

    public ArrayList<Integer> getShortestPath(int x, int y){
        y--;
        ArrayList<Double> prevVertex = getDistancesFromVertex(x, true);
        ArrayList<Double> path = new ArrayList<>();
        //path.add((double)y);
        double current = y;
        for(int i=0; i<prevVertex.size(); i++){
            path.add(current+1);
            if(current==x-1) break;
            current = prevVertex.get((int)current);


        }
        ArrayList<Integer> reversedPath = new ArrayList<>();
        for(int i=path.size()-1; i>=0; i--){
            reversedPath.add(path.get(i).intValue());
        }
        return reversedPath;
    }
}
