package graphs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class UndirectedGraph implements Graph{
	
	private HashSet<HashSet<String>> edges = new HashSet<HashSet<String>>();
	private HashSet<String> vertices = new HashSet<String>();
	private HashMap<String, int[]> vertexPositions = new HashMap<String, int[]>();
	
	public UndirectedGraph(String[][] edges, String[] vertices){

		for(String v: vertices){
			addVertex(v);
		}

		for(String[] e: edges){
			addEdge(e);
		}
		
	}
	
	public UndirectedGraph(String[][] edges){
		this(edges, new String[0]);
	}
	
	
	public UndirectedGraph(String[] vertices){
		this(new String[0][0], vertices);	
	}
	

	@Override
	public HashSet<String> getVertices(){
        HashSet<String> result = new HashSet<String>();
        for(String v: this.vertices){
            result.add(v);
        }
		return result;
	}
	
	public HashSet<HashSet<String>> getEdges(){
		return this.edges;
	}
	
	public void addEdge(String[] edge){ 
		this.edges.add(new HashSet<String>(Arrays.asList(edge)));
		this.vertices.addAll(Arrays.asList(edge));
	}
	
	public void addVertex(String vertex){
		this.vertices.add(vertex);
	}
	
	public HashSet<HashSet<String>> getIncidentEdges(String vertex){
		HashSet<HashSet<String>> result = new HashSet<HashSet<String>>();
		for(HashSet<String> edge: this.edges){
			if(edge.contains(vertex)){
				result.add(edge);
			}
		}
		return result;
	}
	
	public void deleteEdge(String[] edge){
		this.edges.remove(new HashSet<String>(Arrays.asList(edge)));
	}
	
	public void deleteVertex(String vertex){
		for(HashSet<String> edge: getIncidentEdges(vertex)){
			this.edges.remove(edge);
		}
		this.vertices.remove(vertex);
	}
	
	public HashSet<String> neighbors(String vertex){
		HashSet<String> result = new HashSet<String>();
		for(String v: this.vertices){
			if(hasEdge(new String[]{vertex, v})){
				result.add(v);
			}
		}
		return result;
	}
	
	public int degree(String vertex){
		return neighbors(vertex).size();
	}
	
	public boolean hasVertex(String vertex){
        return this.vertices.contains(vertex);
    }

	public boolean hasEdge(String[] edge){
        return this.edges.contains(new HashSet<String>(Arrays.asList(edge)));
    }

    @Override
	public void addVertexPosition(String vertex, int[] position){
		this.vertexPositions.put(vertex, position);
	}

    @Override
	public int[] getVertexPosition(String vertex){
		return this.vertexPositions.get(vertex);
	}

	public double getVertexDistance(String v1, String v2){
		int[] pos1 = getVertexPosition(v1);
		int[] pos2 = getVertexPosition(v2);

        return Math.sqrt(Math.pow(pos1[0]-pos2[0], 2) + Math.pow(pos1[1]-pos2[1], 2));
	}

	
	
	

}
