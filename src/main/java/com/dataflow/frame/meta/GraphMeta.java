package com.dataflow.frame.meta;

import java.util.ArrayList;
import java.util.List;

public abstract class GraphMeta<T extends VertexMeta, U extends EdgeMeta<T>> {

    private List<T> vertexes = new ArrayList<>();
    private List<U> edges = new ArrayList<>();

    public void addVertex(T t) { vertexes.add(t); }

    public void addVertex(List<T> vertexes, T t) {
        // 避免重复添加
        int idx = vertexes.indexOf(t);
        if(idx < 0) vertexes.add(t);
    }

    public void addEdge(U u) { edges.add(u); }

    public void addEdge(List<U> edges, U u) {
        // 避免重复添加
        int idx = edges.indexOf(u);
        if(idx < 0) edges.add(u);
    }

    public T getVertex(int i) { return vertexes.get(i); }

    public U getEdge(int i) { return edges.get(i); }

    public int nrVertex() { return vertexes.size(); }

    public int nrEdge() { return edges.size(); }

    public List<String> getVertexIds() {
        List<String> ids = new ArrayList<>();
        for(T vertex : vertexes) {
            ids.add(vertex.getNodeId());
        }
        return ids;
    }

    public List<String> getVertexIds(List<T> vertexes) {
        List<String> ids = new ArrayList<>();
        for (T vertex : vertexes) {
            ids.add(vertex.getNodeId());
        }
        return ids;
    }

    public T findVertexByID(String id) {
        for (T vertex : vertexes) {
            if(vertex.getNodeId().equals(id)){
                return vertex;
            }
        }
        return null;
    }

    public List<String> getVertexNames() {
        List<String> names = new ArrayList<>();
        for(T vertex : vertexes) {
            names.add(vertex.getNodeName());
        }
        return names;
    }

    public List<String> getVertexNames(List<T> vertexes) {
        List<String> names = new ArrayList<>();
        for (T vertex : vertexes) {
            names.add(vertex.getNodeName());
        }
        return names;
    }

    public T findVertexByName(String name) {
        for (T vertex : vertexes) {
            if(vertex.getNodeName().equals(name)){
                return vertex;
            }
        }
        return null;
    }

    public List<T> getDisabledVertexes() {
        List<T> disabled = new ArrayList<>();
        for (T vertex : vertexes) {
            if(!vertex.isEnabled()) {
                disabled.add(vertex);
            }
        }
        return disabled;
    }

    public List<T> getIsolateVertexes() {
        List<T> isolated = new ArrayList<>();
        for(T vertex : vertexes) {
            if(vertex.isIsolated()) {
                isolated.add(vertex);
            }
        }
        return isolated;
    }

    public List<T> findPreviousVertex(T vertex, boolean all) {
        List<T> previous = new ArrayList<>();
        for(U edge: edges) {
            if ((edge.isEnabled() | all) && edge.getEndNode().equals(vertex)) {
                T t = edge.getStartNode();
                addVertex(previous, t);
            }
        }
        return previous;
    }

    public List<T> findNextVertex(T vertex, boolean all) {
        List<T> next = new ArrayList<>();
        for(U edge: edges) {
            if ((edge.isEnabled() | all) && edge.getStartNode().equals(vertex)) {
                T t = edge.getEndNode();
                addVertex(next, t);
            }
        }
        return next;
    }

    public List<T> getFlowStartVertexes(boolean all,
                                        boolean containIsolateNode) {
        List<T> vertexes = new ArrayList<>();
        for(U edge: edges) {
            T t = edge.getStartNode();
            if(findPreviousVertex(t, all).isEmpty()) {
                addVertex(vertexes, t);
            }
        }
        if(containIsolateNode) {
            List<T> isolationNodes = getIsolateVertexes();
            for (T t: isolationNodes) {
                addVertex(vertexes, t);
            }
        }
        return vertexes;
    }

    public List<T> getFlowEndVertexes(boolean all,
                                      boolean containIsolateNode) {
        List<T> vertexes = new ArrayList<>();
        for(U edge: edges) {
            T t = edge.getStartNode();
            if(findNextVertex(t, all).isEmpty()) {
                addVertex(vertexes, t);
            }
        }
        if(containIsolateNode) {
            List<T> isolationNodes = getIsolateVertexes();
            for (T t: isolationNodes) {
                addVertex(vertexes, t);
            }
        }
        return vertexes;
    }
}
