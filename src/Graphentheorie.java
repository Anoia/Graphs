import graphs.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;


public class Graphentheorie {

	public static void main(String[] arg){
		String[][] edges = {
				{"A", "B"},
				{"B", "C"},
				{"C", "D"},
                {"E", "F"},
				{"A", "F"},
				{"B", "E"},
                {"E", "C"}
			};

        String[][] edgesEuler = {
                {"1", "2"},
                {"1", "3"},
                {"4", "7"},
                {"2", "4"},
                {"7", "2"},
                {"3", "2"},
        };

		UndirectedGraph g = new UndirectedGraph(edges);
        new Renderer(g);
        //eulerCircle(g);


        //g.addVertex("D");
		//g.addEdge(new String[]{"A", "F"});
		//g.deleteEdge(new String[]{"A", "B"});
		//g.deleteVertex("F");
		//g.addEdge(new String[]{"K", "L"});
		//System.out.println(g.getVertices().toString());
		//System.out.println(g.getEdges().toString());
		//System.out.println(g.getIncidentEdges("A").toString());
		//System.out.println("Neighbors of A:"+g.neighbors("A"));
		//System.out.println("Degree of A: "+g.degree("A"));
        //System.out.println("Bridge: " + isBridge(g, new String[]{"1", "2"}));

		//EulerCircle(g);
		//System.out.println("Distance AB:"+g.getVertexDistance("A", "B"));

        //System.out.println("G is connected: "+connected(g));
       // System.out.println("G is eulerian "+ eulerian(g) );

        //eulerCircle(g);


       // DirectedGraph dg = new DirectedGraph(edges);
        //Renderer r = new Renderer(dg);
        //System.out.println("Cycle?"+dg.cycle("7"));

	}





    //Ungerichtete Graphen

    /**
     * Entscheidet mit Hilfe der Breitensuche, ob der Graph g zusammenhängend ist
     * @param g Graph
     * @return true or false
     */
    public static boolean connected(UndirectedGraph g){
        //benötigte Datenstrukturen initialisieren, Startknoten festlegen
        Queue<String> queue = new LinkedList<String>();
        HashSet<String> markedVertices = new HashSet<String>();
        String startVertex = null;
        for(String v: g.getVertices()){
            startVertex = v;
            break;
        }
        //Startknoten markieren und zur queue hinzufügen
        markedVertices.add(startVertex);
        queue.offer(startVertex);

        //solange die warteschlange nicht leer ist:
        //1. Element entfernen und als aktiv markieren
        while(!queue.isEmpty()){
            String active = queue.poll();
            markedVertices.add(active);

            //alle Nachbarn des aktiven knoten untersuchen
            for(String neighbor: g.neighbors(active)){
                //neighbor ist von active aus erreichbar, da sie ja Nachbarn sind
                if(!markedVertices.contains(neighbor)){
                    markedVertices.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }

        //wenn markedVertices = graph.vertices alle knoten des grahen markiert,
        // d.h sie sind vom startknoten aus erreichbar
        return (g.getVertices().equals(markedVertices));
    }

    /**
     * Entscheidet, ob der Graph g eulersch ist
     * Satz 4: Ein Graph ist genau dann eulersch, wenn er
     * zusammenhängend ist und jeder Knoten geraden Grad hat.
     * @param g Graph
     * @return true or false
     */
    public static boolean eulerian(UndirectedGraph g){
        //jeder knoten muss geraden Grad haben
        boolean eul = true;
        for(String v: g.getVertices()){
            if(g.degree(v)%2!=0){
                //Grad von v ist nicht gerade
                eul = false;
                break;
            }
        }

        return eul&&connected(g);
    }

    /**
     * Gibt den Eulerkreis für den eulerschen Graphen G aus
     * verwendet den Fleury
     * @param g eulerian Graph
     */
    public static ArrayList<String> eulerCircle(UndirectedGraph g){
        if(!eulerian(g)){
            //nicht eulerisch
            System.out.println("Cannot compute eulercircle, graph is not eulerian!");
            return null;
        } else{
            //erstellt einen neuen Graphen g2 = g, der zum markieren der Kanten verwendet wird
            // dabei zählt eine vorhandene Kante als nicht markiert
            UndirectedGraph g2 = cloneGraph(g);
            ArrayList<String> path = new ArrayList<String>();

            // I Wähle eine beliebige Ecke vStart aus und betrachte sie als aktuelle Ecke
            String currentVertex = null;
            for(String v: g2.getVertices()){
                currentVertex = v;
                break;
            }
            System.out.println("StartVertex: "+currentVertex);

            while(!g2.getIncidentEdges(currentVertex).isEmpty()){
                System.out.println("still incident edges from startvertex");
                HashSet<String> currentEdge = null;
                // II wähle eine mit currentVertex inzidente kante, die nicht markiert wurde (wenn sie noch in g2 enthalten ist, ist sie nicht markiert)
                for(HashSet<String> edge: g2.getIncidentEdges(currentVertex)){
                    //versuche eine Ecke zu wählen, die keine Brücke ist
                    if(!isBridge(g2, edge.toArray(new String[edge.size()]))){
                        currentEdge = edge;
                        System.out.println("this is not a bridge:");
                        break;
                    }
                }


                if(currentEdge == null){
                    System.out.println("this is a bridge:");
                    //wenn keine Kante, die keine Brücke ist, gewählt werden konnte, wähle eine von den vorher ausgeschlossenen
                    for(HashSet<String> edge: g2.getIncidentEdges(currentVertex)){
                        currentEdge = edge;
                        break;
                    }
                }
                System.out.println("CurrentEdge: "+currentEdge);

                // III Markiere diese Kante und füge sie zu path hinzu. Wähle die andere Ecke dieser Kante als currentVertex
                g2.deleteEdge(currentEdge.toArray(new String[2]));
                path.add(currentVertex);
                for(String v: currentEdge){
                    if(!v.equals(currentVertex)){
                        currentVertex = v;
                        break;
                    }
                }
                System.out.println("new current vertex: "+currentVertex);
                // IV gehe zu II wenn von currentVertex noch unmarkierte kanten ausgehen
            }
            //startvertex nochmal zu path hinzufügen:
            path.add(currentVertex);
            System.out.println("Path: " + path.toString());
            return path;
        }
    }

    /**
     * Akzeptiert den Graphen g und gibt einen graphen g2 zurück, der äquivalent zu g ist.
     * @param g undirected graph
     * @return
     */
    private static UndirectedGraph cloneGraph(UndirectedGraph g) {
        String[][] edges = new String[g.getEdges().size()][2];
        int i = 0;
        for(HashSet<String> edge: g.getEdges()){
            edges[i] = edge.toArray(new String[2]);
            i++;
        }
        i=0;
        String[] vertices = new String[g.getVertices().size()];
        for(String v: g.getVertices()){
            vertices[i] = v;
            i++;
        }
        return new UndirectedGraph(edges, vertices);
    }

    /**
     * Findet heraus, ob die Kante e in dem Graphen G eine Brücke ist
     * Funktioniert nicht, wenn der Graph mehr als eine Zusammenhangskompontene mit Kanten hat, entfernt isolierte Ecken
     * @param g
     * @param edge
     * @return true or false
     */
    public static boolean isBridge(UndirectedGraph g, String[] edge){
        //remove isolated vertices
        for(String v: g.getVertices()){
            if(g.neighbors(v).isEmpty()){
                g.deleteVertex(v);
            }
        }

        //check if g\{edge} is still connected
        // [auf eine Brücke testen in einem Graphen mit mehr als einer Zusammenhangskomponente geht so nicht!]
        g.deleteEdge(edge);
        boolean bridge = false;
        if(!connected(g)){
            bridge = true;
        }
        g.addEdge(edge);
        return bridge;
    }

}
