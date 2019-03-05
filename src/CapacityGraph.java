import java.util.ArrayList;

public class CapacityGraph extends Graph {
    private ArrayList<ArrayList<Double>> capacity = new ArrayList<>();
    private ArrayList<ArrayList<Double>> flow = new ArrayList<>();

    public CapacityGraph(int numOfVertices){

        this.numOfVertices = numOfVertices;
        for(int i=0; i<numOfVertices; i++) {
            ArrayList<Double> row = new ArrayList<>();
            ArrayList<Double> row1 = new ArrayList<>();
            ArrayList<Double> row2 = new ArrayList<>();

            for(int j=0;j<numOfVertices; j++){

                row.add(0.0);
                row1.add(0.0);
                row2.add(0.0);
            }
            edges.add(row);
            capacity.add(row1);
            flow.add(row2);
        }
    }

    public double getCapacity(int i, int j){
        return capacity.get(i-1).get(j-1);
    }

    public void setCapacity(int i, int j, double newCapacity){
        capacity.get(i-1).set(j-1, newCapacity);
        capacity.get(j-1).set(i-1, newCapacity);
    }

    public ArrayList<Double> getDistancesFromVertex(int x, boolean path, double newFlow){
        x--;
        ArrayList<Double> prevVertex = new ArrayList<>();
        ArrayList<Double> shortestDistance = new ArrayList<>();
        ArrayList<Boolean> visited = new ArrayList<>();

        //stworzenie nowej macierzy
        ArrayList<ArrayList<Double>> newFlowMatrix = new ArrayList<>();
        for(int i=0; i<numOfVertices; i++) {
            ArrayList<Double> row = new ArrayList<>();
            for(int j=0;j<numOfVertices; j++){
                row.add(0.0);
            }
            newFlowMatrix.add(row);
        }


        //tam gdzie capacity za male, zwiekszamy flow na max zeby nie szukalo tam sciezek
        for(int i = 0; i< numOfVertices; i++){
            for(int j=0; j<numOfVertices; j++){
                if(flow.get(i).get(j)+newFlow>capacity.get(i).get(j) && edges.get(i).get(j)>0) newFlowMatrix.get(i).set(j, Double.MAX_VALUE);
                else newFlowMatrix.get(i).set(j, flow.get(i).get(j));
            }
        }

        initializeArray(prevVertex, numOfVertices, 0);
        initializeArray(shortestDistance, numOfVertices, Double.MAX_VALUE);
        initializeArray(visited, numOfVertices, false);

        shortestDistance.set(x, 0.0);

        for(int i=0; i<numOfVertices; i++){
            int elem = getIndexOfMinMasked(shortestDistance, visited);
            ArrayList<Integer> adjVert = getAdjacentVertices(elem+1);
            for(int j=0; j<adjVert.size(); j++){
                if(shortestDistance.get(adjVert.get(j)-1)>shortestDistance.get(elem)+newFlowMatrix.get(elem).get(adjVert.get(j)-1) &&
                edges.get(elem).get(adjVert.get(j)-1)>0){
                    shortestDistance.set(adjVert.get(j)-1, shortestDistance.get(elem)+newFlowMatrix.get(elem).get(adjVert.get(j)-1));
                    prevVertex.set(adjVert.get(j)-1, (double)elem);
                    visited.set(elem, true);
                }
            }
        }
        if(path) return prevVertex;
        else return shortestDistance;
    }

    public ArrayList<Integer> getShortestPath(int x, int y, double newFlow){
        y--;
        ArrayList<Double> prevVertex = getDistancesFromVertex(x, true, newFlow);
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

        //sprawdzamy czy rzeczywiscie wartosci sa ponizej capacity
        for(int i=0; i<reversedPath.size()-1; i++){
            int k = reversedPath.get(i);
            int l = reversedPath.get(i+1);
            if(flow.get(k-1).get(l-1)+newFlow > capacity.get(k-1).get(l-1)) return null;
        }

        for(int i=0; i<reversedPath.size()-1; i++){
            int k = reversedPath.get(i);
            int l = reversedPath.get(i+1);
            flow.get(k-1).set(l-1, flow.get(k-1).get(l-1) + newFlow);
        }

        return reversedPath;
    }
}
