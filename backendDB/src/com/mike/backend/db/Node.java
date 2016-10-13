package com.mike.backend.db;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 6/12/2016.
 */
abstract public class Node {

    protected long id;
    protected Node parent = null;
    protected List<Node> children = new ArrayList<>();

    protected Node (Node parent, long id) {
        this.id = id;
        this.parent = parent;
        this.parent.children.add(this);
    }

    public long getID() { return id; }
    public Node getParent() {
        return parent;
    }
    public List<Node> getChildren() {
        return children;
    }

    abstract public String getTag();        // logging TAG, class name...
    abstract public String toString();
}