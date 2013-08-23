package graphs;


import java.util.*;

public class DirectedGraph implements Graph{
    private HashSet<ArrayList<String>> edges = new HashSet<ArrayList<String>>();
    private HashSet<String> vertices = new HashSet<String>();
    private HashMap<String, int[]> vertexPositions = new HashMap<String, int[]>();

    public HashMap<String, String> markedVertices = new HashMap<String, String>();
    public Stack cyclePath = new Stack();

    public DirectedGraph (String[][] edges, String[] vertices){

        for(String v: vertices){
            addVertex(v);
        }

        for(String[] e: edges){
            addEdge(e);
        }

    }

    public DirectedGraph(String[][] edges){
        this(edges, new String[0]);
    }


    public DirectedGraph(String[] vertices){
        this(new String[0][0], vertices);
    }

    @Override
    public HashSet<String> getVertices(){
        return this.vertices;
    }

    public HashSet<ArrayList<String>> getEdges(){
        return this.edges;
    }

    public void addEdge(String[] edge){
        this.edges.add(new ArrayList<String>(Arrays.asList(edge)));
        this.vertices.addAll(Arrays.asList(edge));
    }

    public void addVertex(String vertex){
        this.vertices.add(vertex);
    }

    public HashSet<ArrayList<String>> getIncomingEdges(String vertex){
        HashSet<ArrayList<String>> result = new HashSet<ArrayList<String>>();
        for(ArrayList<String> edge: this.edges){
            if(edge.get(1).equals(vertex)){
                result.add(edge);
            }
        }
        return result;
    }
    public HashSet<ArrayList<String>> getOutgoingEdges(String vertex){
        HashSet<ArrayList<String>> result = new HashSet<ArrayList<String>>();
        for(ArrayList<String> edge: this.edges){
            if(edge.get(0).equals(vertex)){
                result.add(edge);
            }
        }
        return result;
    }

    public void deleteEdge(String[] edge){
        this.edges.remove(new ArrayList<String>(Arrays.asList(edge)));
    }

    public void deleteVertex(String vertex){
        for(ArrayList<String> edge: getIncomingEdges(vertex)){
            this.edges.remove(edge);
        }
        for(ArrayList<String> edge: getOutgoingEdges(vertex)){
            this.edges.remove(edge);
        }
        this.vertices.remove(vertex);
    }

    public HashSet<String> inNeighborhood(String vertex){
        HashSet<String> result = new HashSet<String>();
        for(String v: this.vertices){
            if(hasEdge(new String[]{v, vertex})){
                result.add(v);
            }
        }

        return result;
    }

    public HashSet<String> outNeighborhood(String vertex){
        HashSet<String> result = new HashSet<String>();
        for(String v: this.vertices){
            if(hasEdge(new String[]{vertex, v})){
                result.add(v);
            }
        }

        return result;
    }

    public boolean hasVertex(String vertex){
        return this.vertices.contains(vertex);
    }

    public boolean hasEdge(String[] edge){
        return this.edges.contains(new ArrayList<String>(Arrays.asList(edge)));
    }
    @Override
    public void addVertexPosition(String vertex, int[] position){
        this.vertexPositions.put(vertex, position);
    }
    @Override
    public int[] getVertexPosition(String vertex){
        return this.vertexPositions.get(vertex);
    }

    public int inDegree(String vertex){
        return inNeighborhood(vertex).size();
    }
    public int outDegree(String vertex){
        return outNeighborhood(vertex).size();
    }

    /**
     * Gibt den Euklidischen Abstand von zwei Ecken zurück.
     * @param v1    vertex1
     * @param v2    vertex2
     * @return      euklidischen Abstand von v1, v2
     */
    public double getVertexDistance(String v1, String v2){
        int[] pos1 = getVertexPosition(v1);
        int[] pos2 = getVertexPosition(v2);

        return Math.sqrt(Math.pow(pos1[0]-pos2[0], 2) + Math.pow(pos1[1]-pos2[1], 2));
    }


    /**
     * Berechnet mittels Tiefensuche, ob von einem Vertex v aus ein
     * Zyklus erreichbar ist.
     * @param vertex
     * @return
     */
    public boolean cycle(String vertex){

        // I Wenn v "in Bearbeitung", beende Funktion und gib TRUE zurück
        if(markedVertices.get(vertex).equals("active")){
            cyclePath.push(vertex);
            System.out.println("Path: " + cyclePath.toString());
            return true;
        }
        // II Wenn v "fertig", beende Funktion und gib FALSE zurück
        if(markedVertices.get(vertex).equals("done")){
            return false;
        }

        // III Markiere v als "in Bearbeitung"
        markedVertices.put(vertex, "active");
        cyclePath.push(vertex);

        // IV Rufe für alle Nachfolger u von v nacheinander cycle(u) auf.
        //    Wenn einer dieser Aufrufe TRUE zurück gibt, beende die Funktion und gib TRUE zurück
        for(String u: this.outNeighborhood(vertex)){
            if(cycle(u)){
                return true;
            }
        }

        // V markiere v als "fertig"
        markedVertices.put(vertex, "done");
        cyclePath.pop();

        // VI gib FALSE zurück
        return false;
    }





}
