package org.mxgraph.flow.core;

import org.mxgraph.flow.core.edge.HopMeta;
import org.mxgraph.flow.core.vertex.Vertex;
import org.mxgraph.flow.core.vertex.VertexMeta;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMeta<T extends VertexMeta> {

    private List<T> vertex = new ArrayList<>();
    private List<HopMeta<T>> edge = new ArrayList<>();

    public void addVertex(T t) { vertex.add(t); }
    public void addEdge(HopMeta<T> hopMeta) { edge.add(hopMeta); }
    public T getVertex(int i) { return vertex.get(i); }
    public HopMeta<T> getEdge(int i) { return edge.get(i); }
    public int nrVertex() { return vertex.size(); }
    public int nrEdge() { return edge.size(); }

    public List<String> getVertexIds() {
        List<String> ids = new ArrayList<>();
        for(int i = 0; i < nrVertex(); ++i) {
            ids.add(getVertex(i).getId());
        }
        return ids;
    }

    public List<String> getVertexIds(List<T> t) {
        List<String> ids = new ArrayList<>();
        for (T value : t) {
            ids.add(value.getId());
        }
        return ids;
    }

    public List<String> getVertexNames() {
        List<String> names = new ArrayList<>();
        for(int i = 0; i < nrVertex(); ++i) {
            names.add(getVertex(i).getName());
        }
        return names;
    }

    public List<String> getVertexNames(List<T> t) {
        List<String> names = new ArrayList<>();
        for (T value : t) {
            names.add(value.getName());
        }
        return names;
    }

    public T findVertexByID(String id) {
        for (int i = 0; i < nrVertex(); i++) {
            T vertex = getVertex(i);
            if(vertex.getId().equals(id)){
                return vertex;
            }
        }
        return null;
    }

    public T findVertexByName(String name) {
        for (int i = 0; i < nrVertex(); i++) {
            T vertex = getVertex(i);
            if(vertex.getName().equals(name)){
                return vertex;
            }
        }
        return null;
    }

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

    public List<T> findPreviousVertex(T vertex, boolean all) {
        List<T> previous = new ArrayList<>();
        for(int i = 0; i < nrEdge(); ++i) {
            HopMeta<T> hopMeta = getEdge(i);
            if ((hopMeta.isEnabled() | all) && hopMeta.getTo().equals(vertex)) {
                T t = hopMeta.getFrom();
                int idx = previous.indexOf(t);
                if(idx < 0) previous.add(t);
            }
        }
        return previous;
    }

    public List<T> findNextVertex(T vertex, boolean all) {
        List<T> next = new ArrayList<>();
        for(int i = 0; i < nrEdge(); ++i) {
            HopMeta<T> hopMeta = getEdge(i);
            if ((hopMeta.isEnabled() | all) && hopMeta.getFrom().equals(vertex)) {
                T t = hopMeta.getTo();
                int idx = next.indexOf(t);
                if(idx < 0) next.add(hopMeta.getTo());
            }
        }
        return next;
    }

    public List<T> getStartOrEndVertexes(Vertex status, boolean allowAlone, boolean allowDisabled) {
        List<T> vertexes = new ArrayList<>();
        for(int i = 0; i < nrEdge(); ++i) {
            HopMeta<T> hopMeta = getEdge(i);
            if(hopMeta.isEnabled() || allowDisabled) {
                if(status == Vertex.Start) {
                    T t = hopMeta.getFrom();
                    if(findPreviousVertex(t, allowDisabled).isEmpty()) {
                        int idx = vertexes.indexOf(t);
                        if (idx < 0) vertexes.add(t);
                    }
                }
                if(status == Vertex.End) {
                    T t = hopMeta.getTo();
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
                if(!t.isConnect()) {
                    int idx = vertexes.indexOf(t);
                    if (idx < 0) vertexes.add(t);
                }
            }
        }
        return vertexes;
    }
}
