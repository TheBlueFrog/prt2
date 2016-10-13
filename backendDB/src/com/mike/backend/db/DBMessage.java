package com.mike.backend.db;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

import com.mike.util.MyException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mike on 6/7/2016.
 *
 * class that holds a bot message fished from the DB
 * Messages we parse come from two tables, Messages and MessageData
 * Messages the come pre-parsed (e.g. Alexa) come from MessagesJASON
 */
public class DBMessage {
    private long id;
    private MessageState state;
    private String src;
    private String dst;
    private long time;
    private long dataID;
    private String message = null;
    private String parsed = null;
//    private JSONObject json = null;

    // Messages & MessageData
    public DBMessage(ResultSet rs, ResultSet rs1) throws SQLException {
        id = rs.getLong(1);
        state = MessageState.values()[rs.getInt(2)];
        src = rs.getString(3);
        dst = rs.getString(4);
        time = rs.getLong(5);
        dataID = rs.getLong(6);
        message = rs1.getString(2);
        parsed = rs1.getString(3);
    }

    // MessageJSON
    public DBMessage(ResultSet rs) throws SQLException {
        id = rs.getLong(1);
        state = MessageState.values()[rs.getInt(2)];
        src = rs.getString(3);
        dst = rs.getString(4);
        time = rs.getLong(5);
//        try {
//            JSONParser jp = new JSONParser();
//            json = (JSONObject) jp.parse(rs.getString(6));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

    public long getDataID() {
        return dataID;
    }

    public void setMsg(String msg) {
        this.message = msg;
    }

    public void setParsed(String parsed) {
        this.parsed = parsed;
    }

    public String getParsed() throws SQLException {
        return parsed;
    }

    public void processParsedMsg() throws MyException {
    }

    public String getSrc() {
        return src;
    }
    public String getDst() {
        return dst;
    }

    public boolean setState(MessageState state) throws SQLException {
        this.state = state;

        Set<String> s = new HashSet<String>();
        s.add("State");
        return DB.getDB().updateMessage(this, s);
    }

    public MessageState getState() {
        return state;
    }

    public long getID() {
        return id;
    }

    public long getTime() {
        return time;
    }

    //@TODO // FIXME: 6/16/2016 ugly
    public void jamState(MessageState state) {
        this.state = state;
    }


//    public JSONObject getJSON() {
//        return json;
//    }

//    public boolean isJSON() {
//        return json != null;
//    }
}
