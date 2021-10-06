/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author akil
 */
public class ForecastNode<T> implements Serializable {

    @JsonView({Views.InternalView.class, Views.InternalView.class})
    private int id;
    @JsonView({Views.InternalView.class, Views.InternalView.class})
    private Integer parent;
    @JsonView({Views.InternalView.class, Views.InternalView.class})
    private T payload;
    @JsonIgnore
    private List<ForecastNode<T>> tree;
    @JsonView({Views.InternalView.class, Views.InternalView.class})
    private int level;
    @JsonView({Views.InternalView.class, Views.InternalView.class})
    private String sortOrder;

    public ForecastNode(int id, Integer parent, T payload) {
        this.id = id;
        this.parent = parent;
        this.payload = payload;
        this.tree = new LinkedList<>();
    }

    public ForecastNode(int id) {
        this.id = id;
        this.tree = new LinkedList<>();
        this.level = 0;
        this.sortOrder = "00";
    }

    public ForecastNode() {
        this.tree = new LinkedList<>();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public List<ForecastNode<T>> getTree() {
        return tree;
    }

    public void setTree(List<ForecastNode<T>> tree) {
        this.tree = tree;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    @JsonIgnore
    public boolean hasChild() {
        return !this.tree.isEmpty();
    }

    @JsonIgnore
    public int getNoOfChild() {
        return this.getTree().size();
    }

    @Override
    public String toString() {
        return "Node{" + "sortOrder=" + sortOrder + ", level=" + level + ", id=" + id + ", parent=" + parent + ", payload=" + payload + ", tree=" + tree.size() + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ForecastNode other = (ForecastNode) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
