package com.mike.backend.agents;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

import com.mike.agents.Agent;
import com.mike.agents.Framework;
import com.mike.agents.Message;
import com.mike.backend.db.AbstractMessageNode;
import com.mike.backend.db.DB;
import com.mike.backend.db.RootNode;
import com.mike.backend.model.PhysicalPoint;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 6/12/2016.
 *
 * suck up the messages from the db and build a network of the parsed
 * output
 *
 * eventually this has to move into the db since it'll get too big
 * to have in memory...
 */
public class NetworkLoader extends Agent {
    private static final String TAG = NetworkLoader.class.getSimpleName();

    private RootNode users; // root node
    private List<AbstractMessageNode> backlog = new ArrayList<>();

    @Override
    protected String getClassName() {
        return TAG;
    }

    public NetworkLoader(Framework f, Integer sn) {
        super (f, sn);

        // load the world from the DB
        // block the Framework from starting everything running until
        // we are done
        loadWorld();

        register();
    }

    private void loadWorld() {

        try {
            DB.getDB().getPhysicalPoints (new DB.constructfromDB1() {
                @Override
                public void construct(ResultSet rs) throws SQLException {
                    new PhysicalPoint(rs);
                }
            });
//            for (DBMessage m : v)
//                addToNetwork(m);

        } catch (SQLException e) {
            e.printStackTrace();
        }

//        Log.d(TAG, String.format("Loaded network: %d messages, %d arcs, %d WordNodes",
//                users.getOutgoing().size(),
//                Arc.getCount(),
//                WordNode.getCount()));

        //          dump(users);

    }

    @Override
    protected void onMessage(Message msg) {

        if ((msg.mSender == null) && (((Framework.State) msg.mMessage)).equals(Framework.State.AgentsRunning)) {
            // feed the analyzers all history now that the messages are all available

//            for (AbstractMessageNode m : backlog)
//                send(new Message(this, MessageNodeAgent.class, 0, m));

            backlog.clear();
            return;
        }

//        if (msg.mMessage instanceof DBMessage)
//            try {
//                addToNetwork(((DBMessage) msg.mMessage));
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
    }

//    private void addToNetwork(DBMessage msg) throws SQLException {
//
//        UserNode u = UserNode.findOrAdd(msg.getSrc());
//        if (u == null) {
//            throw new IllegalStateException("Unable to get User " + msg.getSrc());
//        }
//
//// not sure we want to do this, need everything in memory as
//// things now stand, see DB jamState()...
//        // @ToDO  FIXME: 9/7/2016
//
//        msg.setState(MessageState.Loaded);
//
//
//        if (u.getGuid().equals(Constants.BotName)) {
//            Log.d(TAG, String.format("Ignore message from FoodBot, id %d", msg.getID()));
//            return;
//        }
//
//        Arc a = new Arc(users, u);
//
//        AbstractMessageNode amn = null;
//        if (msg.isJSON()) {
//            JSONMessageNode m = new JSONMessageNode(msg);
//            a = new Arc(u, m);
//            amn = m;
//        } else {
//            // a message parsed by TensorFlow code but nothing came out
//            if (msg.getParsed().length() == 0) {
//                Log.d(TAG, String.format("Ignoring message unparsed message, id %d", msg.getID()));
//                // @TODO // FIXME: 6/16/2016
//                return;
//            }
//
//            GrammarMessageNode m = new GrammarMessageNode(msg);
//            a = new Arc(u, m);
//            amn = m;
//        }
//
//        if (mFramework.getState() == Framework.State.AgentsRunning) {
//            // see if this message triggers an immediate action, many messages, like
//            // 'I want peanuts' are simply declarative and may not have any action (yet)
//            send(new Message(this, MessageNodeAgent.class, 0, amn));
//        }
//        else {
//            // not running yet, just hold it
//            backlog.add (amn);
//        }
//
//        amn.dump();
//    }
}