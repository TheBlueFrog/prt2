package com.mike.backend.db;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mike on 9/7/2016.
 */
public abstract class AbstractMessageNode extends Node {

    protected static List<AbstractMessageNode> nodes = new ArrayList<>();
    protected long id;
    protected long time;

    public AbstractMessageNode(DBMessage m) {
        this.id = m.getID();
        this.time = m.getTime();
    }

    static public List<Long> getMsgIds() {
        List<Long> v = new ArrayList<Long>();
        for (AbstractMessageNode m : nodes)
            v.add(m.getID());
        return v;
    }

    static public AbstractMessageNode find(long id) {
        for (AbstractMessageNode m : nodes)
            if (id == m.id)
                return m;

        return null;
    }

    static public List<AbstractMessageNode> getMsgs() {
        return nodes;
    }

    static public void add(AbstractMessageNode n) {
        nodes.add(n);
    }
    /**
     * @return the UserNode for this message
     */
    public UserNode getUser() {
        return (UserNode) incoming.get(0).getFrom();
    }

    public void tellUser(String s) {
        try {
            DB.getDB().insertNewMsg(Constants.BotName, getUser().getGuid(), s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected String timeAsString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyy.mm.dd HH:mm.ssss z");
        return df.format(new Date(time));
    }

    public long getID() {
        return id;
    }
}
