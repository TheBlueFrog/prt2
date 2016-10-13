package com.mike.backend.db;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

/**
 * Created by mike on 6/12/2016.
 */
public class RootNode extends Node {

    private static final String TAG = RootNode.class.getSimpleName();

    public RootNode() {
        super(null, 0);
    }

    @Override
    public String getTag() {
        return "Root";
    }

    @Override
    public String toString() {
        return String.format("{%s}", TAG);
    }

}
