import java.util.ArrayList;

public class CapacityGraph extends Graph {
    public ArrayList<ArrayList<Double>> capacity = new ArrayList<>();
    public ArrayList<ArrayList<Double>> flow = new ArrayList<>();

    ArrayList<ArrayList<ArrayList<Integer>>> paths = new ArrayList<>();
    ArrayList<ArrayList<Integer>> numOfPacketsForStream = new ArrayList<>();

    public CapacityGraph(int numOfVertices){

        this.numOfVertices = numOfVertices;
        for(int i=0; i<numOfVertices; i++) {
            ArrayList<Double> row = new ArrayList<>();
            ArrayList<Double> row1 = new ArrayList<>();
            ArrayList<Double> row2 = new ArrayList<>();
            ArrayList<Integer> row3 = new ArrayList<>();

            for(int j=0;j<numOfVertices; j++){

                row.add(0.0);
                row1.add(0.0);
                row2.add(0.0);
                row3.add(0);
            }
            edges.add(row);
            capacity.add(row1);
            flow.add(row2);
            numOfPacketsForStream.add(row3);
        }

        for(int i=0; i<numOfVertices; i++){
            ArrayList<ArrayList<Integer>> pathArray = new ArrayList<>();
            for(int j=0; j<numOfVertices; j++){
                ArrayList<Integer> path = new ArrayList<>();
                pathArray.add(path);
            }
            paths.add(pathArray);
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

    //liczone od 1
    public ArrayList<Integer> getShortestPath(int x, int y, double newFlow){
        y--;
        ArrayList<Double> prevVertex = getDistancesFromVertex(x, true, newFlow);
        ArrayList<Double> path = new ArrayList<>();

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

        return reversedPath;
    }

    //liczone od 1
    public ArrayList<Integer> setStream(int x, int y, double newFlow){
        ArrayList<Integer> path = getShortestPath(x, y, newFlow);

        if(path!=null){
            for(int i=0; i<path.size()-1; i++){
                int k = path.get(i);
                int l = path.get(i+1);
                flow.get(k-1).set(l-1, flow.get(k-1).get(l-1) + newFlow);
            }
            paths.get(x-1).set(y-1, path);
            numOfPacketsForStream.get(x-1).set(y-1, (int)newFlow);
        }
        return path;
    }

    //liczone od 0
    public void deleteStream(int x, int y){
        ArrayList<Integer> path = paths.get(x).get(y);
        int size = numOfPacketsForStream.get(x).get(y);
        for(int i=0; i<path.size()-1; i++){
            int a = path.get(i);
            int b = path.get(i+1);
            double currentFlow = flow.get(a).get(b);
            if(edges.get(a).get(b)>0) flow.get(a).set(b, currentFlow-size);
        }
    }

    ArrayList<ArrayList<Boolean>> getCrossingStreams(int x, int y){
        ArrayList<ArrayList<Boolean>> foundPaths = new ArrayList<>();

        //initialize
        for(int i=0; i<numOfVertices; i++){
            ArrayList<Boolean> row = new ArrayList<>();
            for(int j=0; j<numOfVertices; j++){
                row.add(false);
            }
            foundPaths.add(row);
        }

        for(int i=0; i<numOfVertices; i++){
            for(int j=0; j<numOfVertices; j++){
                for(int k=0; k<paths.get(i).get(j).size()-1; k++){
                    ArrayList<Integer> path = paths.get(i).get(j);
                    if(path.get(k).equals(path.get(k+1))){
                        foundPaths.get(i).set(j, true);
                        foundPaths.get(j).set(i, true);
                        break;
                    }
                }
            }
        }
        return foundPaths;
    }

    //liczone od 1
    public boolean recalculateStreamsForDamagedEdge(int x, int y){
        boolean succes = true;
        ArrayList<ArrayList<Boolean>> streams = getCrossingStreams(x, y);

        for(int i=0; i<numOfVertices; i++) {
            for (int j = 0; j < numOfVertices; j++) {
                if(streams.get(i).get(j) && i<j) deleteStream(i, j);
            }
        }

        for(int i=0; i<numOfVertices; i++){
            for(int j=0; j<numOfVertices; j++){
                if(streams.get(i).get(i) && i<j) {
                    if(setStream(i+1, j+1, numOfPacketsForStream.get(i).get(j))==null) succes = false;
                }
            }
        }

        return succes;

    }
}
