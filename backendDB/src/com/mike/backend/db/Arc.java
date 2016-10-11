package com.mike.backend.db;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

import com.mike.backend.db.Node;

/**
 * Created by mike on 6/12/2016.
 *
 * Arcs go from a Node to a Node with a given message ID,
 * essentially it's a unique trail from node to node to node
 */
public class Arc {

    static private long arcCount = 0;

    private final Node from;
    private final Node to;

    public Arc(Node from, Node to) {
        arcCount++;

        this.from = from;
        this.to = to;

        from.addOutgoing(this);
        to.addIncoming(this);
    }

    public Node getTo() {
        return to;
    }

    public Node getFrom() { return from; }

    public static long getCount() {
        return arcCount;
    }

//    public List<Arc> next() {
//        return getTo().getOutgoing(getID());
//    }
}
