package com.dataflow.frame.meta;

import java.util.ArrayList;
import java.util.List;

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

    /**
     *
     * @param containDisabledNode 是否包含禁用点
     * @param containIsolatedNode 是否包含孤立点
     * @return ids
     */
    public List<String> getVertexIds(boolean containDisabledNode,
                                     boolean containIsolatedNode) {
        List<String> ids = new ArrayList<>();
        for(T vertex : vertexes) {
            boolean enabled = vertex.isEnabled() | containDisabledNode;
            boolean connected = vertex.isConnected() | containIsolatedNode;
            if (enabled && connected) {
                ids.add(vertex.getNodeId());
            }
        }
        return ids;
    }

    public List<String> getVertexNames() {
        List<String> names = new ArrayList<>();
        for(T vertex : vertexes) {
            names.add(vertex.getNodeName());
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

    public List<T> getIsolateVertexes(boolean all) {
        List<T> isolated = new ArrayList<>();
        for(T vertex : vertexes) {
            if((vertex.isEnabled() | all) && !vertex.isConnected()) {
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

    public List<T> getFlowStartVertexes(boolean containDisabledNode,
                                        boolean containIsolateNode) {
        List<T> vertexes = new ArrayList<>();
        for(U edge: edges) {
            T t = edge.getStartNode();
            if(findPreviousVertex(t, containDisabledNode).isEmpty()) {
                addVertex(vertexes, t);
            }
        }
        if(containIsolateNode) {
            List<T> isolationNodes = getIsolateVertexes(containDisabledNode);
            for (T t: isolationNodes) {
                addVertex(vertexes, t);
            }
        }
        return vertexes;
    }

    public List<T> getFlowEndVertexes(boolean containDisabledNode,
                                      boolean containIsolateNode) {
        List<T> vertexes = new ArrayList<>();
        for(U edge: edges) {
            T t = edge.getStartNode();
            if(findNextVertex(t, containDisabledNode).isEmpty()) {
                addVertex(vertexes, t);
            }
        }
        if(containIsolateNode) {
            List<T> isolationNodes = getIsolateVertexes(containDisabledNode);
            for (T t: isolationNodes) {
                addVertex(vertexes, t);
            }
        }
        return vertexes;
    }
}
