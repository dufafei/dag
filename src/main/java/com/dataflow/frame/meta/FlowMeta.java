package com.dataflow.frame.meta;

import java.util.ArrayList;
import java.util.List;

public abstract class FlowMeta<T extends VertexMeta, U extends EdgeMeta<T>> {

    /**
     *
     */
    private List<T> vertex = new ArrayList<>();

    /**
     *
     */
    private List<U> edge = new ArrayList<>();

    public void addVertex(T t) { vertex.add(t); }
    public void addEdge(U u) { edge.add(u); }
    public T getVertex(int i) { return vertex.get(i); }
    public U getEdge(int i) { return edge.get(i); }
    public int nrVertex() { return vertex.size(); }
    public int nrEdge() { return edge.size(); }

    /**
     *
     * @return
     */
    public List<String> getVertexIds() {
        List<String> ids = new ArrayList<>();
        for(int i = 0; i < nrVertex(); ++i) {
            ids.add(getVertex(i).getNodeId());
        }
        return ids;
    }

    /**
     *
     * @param t
     * @return
     */
    public List<String> getVertexIds(List<T> t) {
        List<String> ids = new ArrayList<>();
        for (T value : t) {
            ids.add(value.getNodeId());
        }
        return ids;
    }

    /**
     *
     * @return
     */
    public List<String> getVertexNames() {
        List<String> names = new ArrayList<>();
        for(int i = 0; i < nrVertex(); ++i) {
            names.add(getVertex(i).getNodeName());
        }
        return names;
    }

    /**
     *
     * @param t
     * @return
     */
    public List<String> getVertexNames(List<T> t) {
        List<String> names = new ArrayList<>();
        for (T value : t) {
            names.add(value.getNodeName());
        }
        return names;
    }

    /**
     *
     * @param id
     * @return
     */
    public T findVertexByID(String id) {
        for (int i = 0; i < nrVertex(); i++) {
            T vertex = getVertex(i);
            if(vertex.getNodeId().equals(id)){
                return vertex;
            }
        }
        return null;
    }

    /**
     *
     * @param name
     * @return
     */
    public T findVertexByName(String name) {
        for (int i = 0; i < nrVertex(); i++) {
            T vertex = getVertex(i);
            if(vertex.getNodeName().equals(name)){
                return vertex;
            }
        }
        return null;
    }

    /**
     *
     * @return
     */
    public List<T> getDisabledVertex() {
        List<T> disabled = new ArrayList<>();
        for (int i = 0; i < nrVertex(); i++) {
            T vertex = getVertex(i);
            if(!vertex.isEnabled()) {
                disabled.add(vertex);
            }
        }
        return disabled;
    }

    /**
     *
     * @param vertex
     * @param allowDisabled
     * @return
     */
    public List<T> findPreviousVertex(T vertex, boolean allowDisabled) {
        List<T> previous = new ArrayList<>();
        for(int i = 0; i < nrEdge(); ++i) {
            EdgeMeta<T> edgeMeta = getEdge(i);
            if ((edgeMeta.isEnabled() | allowDisabled) && edgeMeta.getEndNode().equals(vertex)) {
                T t = edgeMeta.getStartNode();
                int idx = previous.indexOf(t);
                if(idx < 0) previous.add(t);
            }
        }
        return previous;
    }

    /**
     *
     * @param vertex
     * @param allowDisabled
     * @return
     */
    public List<T> findNextVertex(T vertex, boolean allowDisabled) {
        List<T> next = new ArrayList<>();
        for(int i = 0; i < nrEdge(); ++i) {
            EdgeMeta<T> edgeMeta = getEdge(i);
            if ((edgeMeta.isEnabled() | allowDisabled) && edgeMeta.getStartNode().equals(vertex)) {
                T t = edgeMeta.getEndNode();
                int idx = next.indexOf(t);
                if(idx < 0) next.add(t);
            }
        }
        return next;
    }

    public List<T> getStartOrEndVertexes(Vertex status, boolean allowAlone, boolean allowDisabled) {
        List<T> vertexes = new ArrayList<>();
        for(int i = 0; i < nrEdge(); ++i) {
            EdgeMeta<T> edgeMeta = getEdge(i);
            if(edgeMeta.isEnabled() || allowDisabled) {
                if(status == Vertex.Start) {
                    T t = edgeMeta.getStartNode();
                    if(findPreviousVertex(t, allowDisabled).isEmpty()) {
                        int idx = vertexes.indexOf(t);
                        if (idx < 0) vertexes.add(t);
                    }
                }
                if(status == Vertex.End) {
                    T t = edgeMeta.getEndNode();
                    if(findNextVertex(t, allowDisabled).isEmpty()) {
                        int idx = vertexes.indexOf(t);
                        if (idx < 0) vertexes.add(t);
                    }
                }
            }
        }
        if(allowAlone) {
            for(int i=0; i < nrVertex(); i++) {
                T t = getVertex(i);
                if(!t.isIsolatedNode()) {
                    int idx = vertexes.indexOf(t);
                    if (idx < 0) vertexes.add(t);
                }
            }
        }
        return vertexes;
    }
}
