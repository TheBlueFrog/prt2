package com.mike.backend.model;

/**
 Created by mike on 10/12/2016.
 */
abstract public class PhysicalObject {

    abstract String getTag();

    protected long id;

    public PhysicalObject(long id) {
        this.id = id;
    }
}
