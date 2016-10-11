package com.mike.backend.db;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

//import com.mike.backend.Constants;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 6/12/2016.
 */
public class UserNode extends Node {

    private static final String TAG = UserNode.class.getSimpleName();

    static private List<UserNode> nodes = new ArrayList<>();

    public static long getCount() {
        return nodes.size();
    }

    static public UserNode findUser(String guid) throws SQLException {
        // look in cache
        for (UserNode n : nodes) {
            if (n.guid.equals(guid))
                return n;
        }
        // not there, look in db
//        final UserNode[] u = new UserNode[1];
        UserNode u = DB.getDB().getUser(guid, new DB.constructfromDB() {
            @Override
            public Object construct(ResultSet rs) throws SQLException {
//                u[0] = new UserNode(rs);
                return new UserNode(rs); //u[0];
            }
        });
        // now look in cache
        for (UserNode n : nodes) {
            if (n.guid.equals(guid))
                return n;
        }

        assert false; // wtf
        return null;
    }
    static public UserNode findOrAdd(String guid) {
        final UserNode[] u = new UserNode[1];
        try {
            u[0] = findUser(guid);
            if (u[0] == null) {
                DB.getDB().insertUser(guid);
                DB.getDB().getUser(guid, new DB.constructfromDB() {
                    @Override
                    public Object construct(ResultSet rs) throws SQLException {
                        u[0] = new UserNode(rs);
                        return u[0];
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u[0];
    }


    private long id;
    private String guid;
    private String name;

    public UserNode(ResultSet rs) throws SQLException {
        this.id = rs.getLong(1);
        this.guid = rs.getString(2);
        this.name = rs.getString(3);
        nodes.add(this);
    }

    public String getGuid() {
        return guid;
    }
    public String getName() {
        return name;
    }

    @Override
    public String dump() { return String.format("%s %s", TAG, toString()); }

    @Override
    public String toString () { return String.format("%s, (%s)", this.getGuid(), this.getName());}

    public void setName(String name) {
        this.name = name;

        try {
            DB.getDB().setUser(getGuid(), "userName", name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void tell(String s) {
        try {
            DB.getDB().insertNewMsg(Constants.BotName, getGuid(), s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long getID() {
        return id;
    }
}
