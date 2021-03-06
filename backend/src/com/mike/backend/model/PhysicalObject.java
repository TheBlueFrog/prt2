package com.mike.backend.model;

import com.mike.backend.Constants;
import com.mike.backend.db.Node;

import java.awt.*;

/**
 Created by mike on 10/12/2016.
 */
abstract public class PhysicalObject
        extends Node
{
    public abstract String getTag();

    public PhysicalObject(Node parent, long id) {
        super(parent, id);
    }
}
