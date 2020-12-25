package com.dataflow.frame.meta;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @param <T>
 * @param <U>
 */
public abstract class FlowMeta<T extends VertexMeta, U extends EdgeMeta<T>> {

    private List<T> vertexes = new ArrayList<>();
    private List<U> edges = new ArrayList<>();

    public void addVertex(T t) { vertexes.add(t); }

    public void addVertex(List<T> vertexes, T t) {
        int idx = vertexes.indexOf(t);
        if(idx < 0) vertexes.add(t);
    }

    public void addEdge(U u) { edges.add(u); }

    public void addEdge(List<U> edges, U u) {
        int idx = edges.indexOf(u);
        if(idx < 0) edges.add(u);
    }

    public int nrVertex() { return vertexes.size(); }

    public int nrEdge() { return edges.size(); }

    public T getVertex(int i) { return vertexes.get(i); }

    public U getEdge(int i) { return edges.get(i); }

    public List<String> getVertexIds(List<T> vertexes) {
        List<String> ids = new ArrayList<>();
        for(T vertex : vertexes) {
            ids.add(vertex.getNodeId());
        }
        return ids;
    }

    public List<String> getVertexNames(List<T> vertexes) {
        List<String> names = new ArrayList<>();
        for(T vertex : vertexes) {
            names.add(vertex.getNodeName());
        }
        return names;
    }

    public List<String> getVertexIndexes(List<T> vertexes) {
        List<String> names = new ArrayList<>();
        for(T vertex : vertexes) {
            names.add(vertex.getIndex());
        }
        return names;
    }

    public T findVertexByID(String id) {
        for (T vertex : vertexes) {
            if(vertex.getNodeId().equals(id)){
                return vertex;
            }
        }
        return null;
    }

    public T findVertexByName(String name) {
        for (T vertex : vertexes) {
            if(vertex.getNodeName().equals(name)){
                return vertex;
            }
        }
        return null;
    }

    public List<T> findPreviousVertexes(T vertex, boolean all) {
        List<T> previous = new ArrayList<>();
        for(U edge: edges) {
            if ((edge.isEnabled() | all) && edge.getEndNode().equals(vertex)) {
                T t = edge.getStartNode();
                addVertex(previous, t);
            }
        }
        return previous;
    }

    public List<T> findNextVertexes(T vertex, boolean all) {
        List<T> next = new ArrayList<>();
        for(U edge: edges) {
            if ((edge.isEnabled() | all) && edge.getStartNode().equals(vertex)) {
                T t = edge.getEndNode();
                addVertex(next, t);
            }
        }
        return next;
    }

    public List<T> findStartVertexes(boolean all) {
        List<T> vertexes = new ArrayList<>();
        for(U edge: edges) {
            T t = edge.getStartNode();
            if(findPreviousVertexes(t, all).isEmpty()) {
                addVertex(vertexes, t);
            }
        }
        return vertexes;
    }

    public List<T> findEndVertexes(boolean all) {
        List<T> vertexes = new ArrayList<>();
        for(U edge: edges) {
            T t = edge.getStartNode();
            if(findNextVertexes(t, all).isEmpty()) {
                addVertex(vertexes, t);
            }
        }
        return vertexes;
    }

    public List<T> findDisabledVertexes() {
        List<T> all = new ArrayList<>();
        for(T vertex: vertexes) {
            if(!vertex.isEnabled()) {
                addVertex(all, vertex);
            }
        }
        return all;
    }

    public List<T> findIsolatedVertexes() {
        List<T> all = new ArrayList<>();
        for(T vertex: vertexes) {
            if(!vertex.isConnected()) {
                addVertex(all, vertex);
            }
        }
        return all;
    }
}
