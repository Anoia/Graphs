package graphs;

import java.util.HashSet;

public interface Graph {
    public abstract void addVertexPosition(String vertex, int[] position);
    public abstract int[] getVertexPosition(String vertex);
    public abstract HashSet<String> getVertices();
}
