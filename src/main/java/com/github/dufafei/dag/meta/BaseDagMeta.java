package com.github.dufafei.dag.meta;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDagMeta<T extends BaseNodeMeta, U extends BaseHopMeta<T>> {

    private List<T> nodes = new ArrayList<>();
    private List<U> hops = new ArrayList<>();

    public BaseDagMeta() {}

    public abstract T getNode();

    public abstract U getHop();

    public List<T> getNodes() { return nodes; }

    public List<U> getHops() { return hops; }

    public int nrNodes() { return nodes.size(); }

    public int nrHops() { return hops.size(); }

    public T findNode(List<T> nodes, String name) {
        for (T node : nodes) {
            if(node.getName().equals(name)){
                return node;
            }
        }
        return null;
    }

    public T findNode(String name) {
        return findNode(nodes, name);
    }

    public void addNode(T t) {
        if (findNode(t.getName()) != null) {
            throw new IllegalArgumentException("Node already exists: " + t);
        } else {
            nodes.add(t);
        }
    }

    public U findHop(List<U> hops, String source, String target) {
        for (U hop : hops) {
            String s = hop.getSource().getName();
            String t = hop.getTarget().getName();
            if(s.equals(source) && t.equals(target)){
                return hop;
            }
        }
        return null;
    }

    public U findHop(String source, String target) {
        return findHop(hops, source, target);
    }

    public void addHop(U u) {
        if (findHop (
                u.getSource().getName(),
                u.getTarget().getName()
        ) != null) {
            throw new IllegalArgumentException("Hop already exists: " + u);
        } else {
            hops.add(u);
        }
    }

    public List<T> findDisabledNodes() {
        List<T> disabled = new ArrayList<>();
        for(T node: nodes) {
            if(!node.getEnabled()) {
                if(findNode(disabled, node.getName()) == null) {
                    disabled.add(node);
                }
            }
        }
        return disabled;
    }

    public List<T> findUnconnectedNodes() {
        List<T> unconnected = new ArrayList<>();
        for(T node: nodes) {
            if(!node.getConnected()) {
                if(findNode(unconnected, node.getName()) == null) {
                    unconnected.add(node);
                }
            }
        }
        return unconnected;
    }

    public List<T> findPreviousNodes(T node, boolean all) {
        List<T> previous = new ArrayList<>();
        for(U hop: hops) {
            if ((hop.getEnabled() | all) && hop.getTarget().equals(node)) {
                T t = hop.getSource();
                previous.add(t);
            }
        }
        return previous;
    }

    public List<T> findNextNodes(T node, boolean all) {
        List<T> next = new ArrayList<>();
        for(U hop: hops) {
            if ((hop.getEnabled() | all) && hop.getSource().equals(node)) {
                T t = hop.getTarget();
                next.add(t);
            }
        }
        return next;
    }

    public List<T> findStartNodes(boolean all) {
        List<T> start = new ArrayList<>();
        for(U hop: hops) {
            T t = hop.getSource();
            if(findPreviousNodes(t, all).isEmpty()) {
                if(findNode(start, t.getName()) == null) {
                    start.add(t);
                }
            }
        }
        return start;
    }

    public List<T> findEndNodes(boolean all) {
        List<T> end = new ArrayList<>();
        for(U hop: hops) {
            T t = hop.getTarget();
            if(findNextNodes(t, all).isEmpty()) {
                if(findNode(end, t.getName()) == null) {
                    end.add(t);
                }
            }
        }
        return end;
    }
}
