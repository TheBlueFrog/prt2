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

    protected List<Arc> incoming = new ArrayList<>();
    protected List<Arc> outgoing = new ArrayList<>();

    public void addIncoming(Arc arc) {
        incoming.add(arc);
    }

    public void addOutgoing(Arc arc) {
        outgoing.add(arc);
    }

    public List<Arc> getIncoming() {
        return incoming;
    }

    public List<Arc> getOutgoing() {
        return outgoing;
    }


    abstract public String dump();
}