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

    public RootNode(String tag) {
    }

    @Override
    public String dump() {
        return String.format("%s", TAG);
    }

//    public UserNode findOrAddUser(String guid) {
//        for (UserNode u : nodes)
//            if (u.getGuid().equals(guid))
//                return u;
//
//        UserNode u = new UserNode(guid);
//        nodes.add(u);
//        return u;
//    }
}
