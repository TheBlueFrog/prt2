package com.mike.backend.db;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

import com.mike.util.AbstractDB;
import com.mike.util.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by mike on 6/7/2016.
 */
public class DB extends AbstractDB {

    private static DB db = null;
    public static DB getDB() { return db; }

    public interface constructfromDB1 {
        /**
         * @param rs
         * @throws SQLException
         */
        public void construct(ResultSet rs) throws SQLException;
    }

    public DB (String dbname) throws SQLException, ClassNotFoundException {
        super (dbname, DB.class.getSimpleName());

        db = this;  // singleton
    }

//    public List<DBMessage> getNewMessages() throws SQLException {
//        List<DBMessage> v = _getMessages(MessageState.New.ordinal());
//        v.addAll(_getJSONMessages(MessageState.New.ordinal()));
//        // sort into time order
////        v.get(0).getTime();
//        return v;
//    }

    /**
     * get ALL messages in the db, and jam their state to new, this
     * should only be used during the startup phase to get the in-memory
     * and in-db sync'd
     *
     * @return
     * @throws SQLException
     */
//    public List<DBMessage> getMessages() throws SQLException {
//        // @TODO // FIXME: 6/16/2016 sets up difference between in-memory and in-db
//        List<DBMessage> v = _getMessages(-1);
//        v.addAll(_getJSONMessages(-1));
//        for (DBMessage m : v)
//            m.jamState (MessageState.New);
//        // sort into time order
////        v.get(0).getTime();
//        return v;
//    }

    // Some messages come via Alexa and preparsed JSON, pull those out too
    /*
    CREATE TABLE MessageJSON (
        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
        State INTEGER,
        Src TEXT, Dst TEXT,
        Time INTEGER,
        MessageData INTEGER);
     */
//    private List<DBMessage> _getJSONMessages(int state) throws SQLException {
//        List<DBMessage> v = new ArrayList<>();
//        PreparedStatement s = null;
//        try {
//            String q = "";
//            switch (state) {
//                case -1:
//                    q = String.format("select * from %s order by Time", "MessageJSON");
//                    break;
//                default:
//                    q = String.format("select * from %s where (State = %d) order by Time", "MessageJSON", state);
//                    break;
//            }
//
//            s = mDB.prepareStatement(q);
//            ResultSet rs = s.executeQuery();
//            while (rs.next()) {
//                    v.add(new DBMessage(rs));
//            }
//            return v;
//        } finally {
//            cleanup(s);
//        }
//    }

//    private List<DBMessage> _getMessages(int state) throws SQLException {
//
//        List<DBMessage> v = new ArrayList<>();
//        PreparedStatement s = null;
//        PreparedStatement s1 = null;
//        try {
//            String q = "";
//            switch (state) {
//                case -1:
//                    q = String.format("select * from %s order by Time", "Messages");
//                    break;
//                default:
//                    q = String.format("select * from %s where (State = %d) order by Time", "Messages", state);
//                    break;
//            }
//
//            s = mDB.prepareStatement(q);
//            ResultSet rs = s.executeQuery();
//            while (rs.next()) {
//
//                String q1 = String.format("select * from %s where (id = %s)", "MessageData", rs.getLong(1));
//                s1 = mDB.prepareStatement(q1);
//                ResultSet rs1 = s1.executeQuery();
//                if (rs1.next()) {
//                    v.add(new DBMessage(rs, rs1));
//                }
//                cleanup(s1);
//            }
//            return v;
//        } finally {
//            cleanup(s);
//        }
//
////        throw new IllegalStateException(String.format("Failed to read last record from table %s", tableName));
//    }
//    public void getMessageData(long id, constructfromDB c) throws SQLException {
//        PreparedStatement s = null;
//        try {
//            String q = String.format("select * from %s where (id = %s)", "MessageData", id);
//            s = mDB.prepareStatement(q);
//            ResultSet rs = s.executeQuery();
//            if (rs.next()) {
//                c.construct(rs);
//            }
//        } finally {
//            cleanup(s);
//        }
//
////        throw new IllegalStateException(String.format("Failed to read last record from table %s", tableName));
//    }

//    public boolean updateMessage(DBMessage m, Set<String> info) throws SQLException {
//        PreparedStatement s = null;
//        if (info.size() > 1)
//            Log.e(TAG, "updateMessage info param is ignored, State assumed");
//
//        try {
//            String table = "";//m.isJSON() ? "MessageJSON" : "Messages";
//            String q = null;
//
//            if (info.contains("State"))
//                q = String.format("update %s set State = %d where id = %d",
//                        table,
//                        m.getState().ordinal(),
//                        m.getID());
//
//            s = mDB.prepareStatement(q);
//            boolean b = s.execute();
//
//            commit();
//            return true; //b = s.execute();
////            return b;
//        } finally {
//            cleanup(s);
//        }
//    }

//    INSERT INTO "Messages" VALUES(null,0,'mike','bot',1,1);
//    INSERT INTO "MessageData" VALUES(null,'raw msg','parsed msg');

//    /**
//     * insert a new message into the DB
//     *
//     * @param src       source user GUID
//     * @param dst       destination use GUID
//     * @param msg       the message
//     * @param parsedMsg the parsed message
//     * @return
//     * @throws SQLException
//     */
//    public boolean insertNewMsg(String src, String dst, String msg, String parsedMsg) throws SQLException {
//        PreparedStatement s = null;
//        PreparedStatement s1 = null;
//        try {
//            String q = String.format("insert into MessageData values (null, \"%s\", \"%s\")", msg, parsedMsg);
//            s = mDB.prepareStatement(q);
//            boolean b = s.execute();
//            long rowid = s.getGeneratedKeys().getInt(1);
//
//            long now = System.currentTimeMillis();
//            String q1 = String.format("insert into Messages values (null, 0, \"%s\", \"%s\", %d, %d)", src, dst, now, rowid);
//            s1 = mDB.prepareStatement(q1);
//            boolean b1 = s1.execute();
//
//            commit();
//            return true; // always false? b && b1;
//        } finally {
//            cleanup(s);
//            cleanup(s1);
//        }
//    }

//    public long insertConsumer(String guid) throws SQLException {
//        return _insertConsumerSupplier("Consumer", guid);
//    }
//    public long insertSupplier(String guid) throws SQLException {
//        return _insertConsumerSupplier("Supplier", guid);
//    }
//
//    private long _insertConsumerSupplier(String table, String guid) throws SQLException {
//        PreparedStatement s = null;
//        try {
//            long now = System.currentTimeMillis();
//            String q = String.format("insert into %s values (null, \"%s\", %d, %d, %d, %d)", table, guid, now);
//            s = mDB.prepareStatement(q);
//            boolean b = s.execute();
//            long rowid = s.getGeneratedKeys().getInt(1);
//
//            commit();
//            return rowid;
//        } finally {
//            cleanup(s);
//        }
//    }

//    public List<ConsumerItem> getConsumerItems(UserNode user) throws SQLException {
//        List<ConsumerItem> v = new ArrayList<>();
//        _getListedItems("ConsumerListingData", user, new DB.constructfromDB1() {
//            @Override
//            public void construct(ResultSet rs) throws SQLException {
//                v.add(new ConsumerItem(user, rs));
//            }
//        });
//        return v;
//    }
//    public List<SupplierItem> getSupplierItems(UserNode user) throws SQLException {
//        List<SupplierItem> v = new ArrayList<>();
//        _getListedItems("SupplierListingData", user, new DB.constructfromDB1() {
//            @Override
//            public void construct(ResultSet rs) throws SQLException {
//                v.add(new SupplierItem(user, rs));
//            }
//        });
//        return v;
//    }
//
//    private void _getListedItems(String table, UserNode user, constructfromDB1 cb) throws SQLException {
//        PreparedStatement s = null;
//        try {
//            String q = String.format("select * from %s where (OwnerID = %d)", table, user.getID());
//            s = mDB.prepareStatement(q);
//            ResultSet rs = s.executeQuery();
//
//            while (rs.next())
//                cb.construct(rs);
//            commit();
//        } finally {
//            cleanup(s);
//        }
//    }

    public void getPhysicalPoints(constructfromDB1 cb) throws SQLException {
        String q = String.format("select * from %s", "PhysicalPoints");
        getObjects(cb, q);
    }
    public void getGuides(constructfromDB1 cb) throws SQLException {
        String q = String.format("select * from %s", "Guides");
        getObjects(cb, q);
    }

    private void getObjects (constructfromDB1 cb, String q) throws SQLException {
        PreparedStatement s = null;
        try {
            s = mDB.prepareStatement(q);
            ResultSet rs = s.executeQuery();

            while (rs.next())
                cb.construct(rs);
            commit();
        } finally {
            cleanup(s);
        }
    }


//    private long findOrInsertItem(String text) throws SQLException {
//        long rowid = getItemDataID(text);
//        if (rowid >= 0)
//            return rowid;
//
//        PreparedStatement s = null;
//        try {
//            String q = String.format("insert into ItemData values (null, \"%s\")", text);
//            s = mDB.prepareStatement(q);
//            boolean b = s.execute();
//            rowid = s.getGeneratedKeys().getInt(1);
//            commit();
//            return rowid;
//        } finally {
//            cleanup(s);
//        }
//    }
//    public long getItemDataID(String text) throws SQLException {
//        PreparedStatement s = null;
//        try {
//            String q = String.format("select id from ItemData where (Item = '%s')", text);
//            s = mDB.prepareStatement(q);
//            ResultSet rs = s.executeQuery();
//            if (rs.next())
//                return rs.getLong(1);
//            else
//                return -1;
//        } finally {
//            cleanup(s);
//        }
//    }

//    public String getItemDataText(long id) throws SQLException {
//        PreparedStatement s = null;
//        try {
//            String q = String.format("select Item from ItemData where (id = %d)", id);
//            s = mDB.prepareStatement(q);
//            ResultSet rs = s.executeQuery();
//            if (rs.next())
//                return rs.getString(1);
//            else
//                return null;
//        } finally {
//            cleanup(s);
//        }
//    }

//    /*
//     */
//    public boolean insertUser(String guid) throws SQLException {
//        PreparedStatement s = null;
//        try {
//            String q = String.format("insert into Users values (null, \"%s\", \"\")", guid);
//            s = mDB.prepareStatement(q);
//            boolean b = s.execute();
//            long rowid = s.getGeneratedKeys().getInt(1);
//            commit();
//            return true; // always false? b && b1;
//        } finally {
//            cleanup(s);
//        }
//    }
//    /**
//     * construct user from DB
//     *
//     * @param guid
//     * @return
//     */
//    public UserNode getUser(String guid, constructfromDB c) throws SQLException {
//        PreparedStatement s = null;
//        try {
//            String q = String.format("select * from %s where (GUID = '%s')", "Users", guid);
//            s = mDB.prepareStatement(q);
//            ResultSet rs = s.executeQuery();
//            if (rs.next())
//                return (UserNode) c.construct(rs);
//            return null;
//        } finally {
//            cleanup(s);
//        }
//
////        throw new IllegalStateException(String.format("Failed to read last record from table %s", tableName));
//    }
//
//    /**
//     *
//     * @param src   user guid
//     * @param name  the attribute of the user to set
//     * @param value the value to set
//     * @return
//     * @throws SQLException
//     */
//    public boolean setUser(String src, String name, String value) throws SQLException {
//        PreparedStatement s = null;
//        try {
//            String q = String.format("update Users set %s = '%s' where GUID = '%s'", name, value, src);
//            s = mDB.prepareStatement(q);
//            boolean b = s.execute();
//
//            commit();
//            return true; // always false? b && b1;
//        } finally {
//            cleanup(s);
//        }
//    }


}
