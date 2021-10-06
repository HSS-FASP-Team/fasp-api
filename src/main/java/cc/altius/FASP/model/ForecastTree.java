/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.model;

import cc.altius.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author akil
 */
public class ForecastTree<T> {

    public ForecastTree(ForecastNode<T> root) {
        this.root = root;
        this.root.setLevel(0);
        this.root.setSortOrder("00");
        flatList = new LinkedList<>();
        flatList.add(root);
    }

    private final ForecastNode<T> root;
    private List<ForecastNode<T>> flatList = new LinkedList<>();

    public void addForecastNode(ForecastNode<T> n) throws Exception {
        if (n.getParent() == null) {
            throw new Exception("Must have a valid Parent");
        }
        ForecastNode<T> checkForecastNode = new ForecastNode<>(n.getParent());
        int idx = this.flatList.indexOf(checkForecastNode);
        if (idx == -1) {
            throw new Exception("Must have a valid Parent Id");
        }
        ForecastNode<T> foundForecastNode = findForecastNode(this.root, n.getParent());
        if (foundForecastNode != null) {
            n.setLevel(foundForecastNode.getLevel() + 1);
            String newSortOrder = foundForecastNode.getSortOrder() + ".";
            newSortOrder += StringUtils.pad(Integer.toString(foundForecastNode.getNoOfChild() + 1), '0', 2, StringUtils.LEFT);
            n.setSortOrder(newSortOrder);
            foundForecastNode.getTree().add(n);
        } else {
            n.setLevel(this.root.getLevel() + 1);
            String newSortOrder = this.root.getSortOrder() + ".";
            newSortOrder += StringUtils.pad(Integer.toString(this.root.getNoOfChild() + 1), '0', 2, StringUtils.LEFT);
            n.setSortOrder(newSortOrder);
            this.root.getTree().add(n);
        }
        this.flatList.add(n);
    }

    public ForecastNode<T> findForecastNode(int id) {
        ForecastNode<T> n = this.root;
        return findForecastNode(n, id);
    }

    private ForecastNode<T> findForecastNode(ForecastNode<T> node, int id) {
        if (node.getId() == id) {
            return node;
        }
        for (ForecastNode<T> n : node.getTree()) {
            if (n.getId() == id) {
                return n;
            } else if (!n.getTree().isEmpty()) {
                ForecastNode foundForecastNode = findForecastNode(n, id);
                if (foundForecastNode != null) {
                    return foundForecastNode;
                }
            }
        }
        return null;
    }

    @JsonIgnore
    public ForecastNode<T> getTreeRoot() {
        return this.root;
    }

    @JsonView({Views.InternalView.class, Views.InternalView.class})
    public List<ForecastNode<T>> getFlatList() {
        return this.flatList;
    }

    public List<T> getPayloadSubList(int id, boolean includeSelf, int level) {
        List<T> subList = new LinkedList<>();
        ForecastNode<T> n = findForecastNode(id);
        if (includeSelf) {
            subList.add(n.getPayload());
        }
        if (level == -1 || level > 0) {
            n.getTree().forEach((child) -> {
                getPayloadSubList(child, subList, (level > 0 ? level - 1 : level));
            });
        }
        return subList;
    }

    @JsonView({Views.InternalView.class, Views.InternalView.class})
    public List<ForecastNode<T>> getTreeFullList() {
        return getTreeSubList(1, true, -1);
    }

    public List<ForecastNode<T>> getTreeSubList(int id, boolean includeSelf, int level) {
        List<ForecastNode<T>> subList = new LinkedList<>();
        ForecastNode<T> n = findForecastNode(id);
        if (includeSelf) {
            subList.add(n);
        }
        if (level == -1 || level > 0) {
            n.getTree().forEach((child) -> {
                getTreeSubList(child, subList, (level > 0 ? level - 1 : level));
            });
        }
        return subList;
    }

    private void getPayloadSubList(ForecastNode<T> n, List<T> subList, int level) {
        subList.add(n.getPayload());
        if (level == -1 || level > 0) {
            n.getTree().forEach((child) -> {
                getPayloadSubList(child, subList, (level > 0 ? level - 1 : level));
            });
        }
    }

    private void getTreeSubList(ForecastNode<T> n, List<ForecastNode<T>> subList, int level) {
        subList.add(n);
        if (level == -1 || level > 0) {
            n.getTree().forEach((child) -> {
                getTreeSubList(child, subList, (level > 0 ? level - 1 : level));
            });
        }
    }
}
