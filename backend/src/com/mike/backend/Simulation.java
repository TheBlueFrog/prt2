package com.mike.backend;

import com.mike.agents.Agent;
import com.mike.agents.Clock;
import com.mike.agents.Framework;
import com.mike.agents.Message;
import com.mike.backend.db.DB;
import com.mike.backend.db.RootNode;
import com.mike.backend.model.*;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 Created by mike on 10/14/2016. */
public class Simulation extends Agent {

    static public void paint(Graphics2D g2) {
//        Log.d(TAG, "in paint");
        PhysicalPoint.paint(g2);
        Guide.paint(g2);
        ObjectOnGuide.paint(g2);
    }

    private RootNode root = null;

    @Override
    protected String getClassName() { return Simulation.class.getSimpleName(); }

    public Simulation(Framework f, Integer serialNumber) {
        super(f, serialNumber);

        // ensure we are a singleton
        assert serialNumber == 0;

        root = new RootNode();
        loadWorld();

        register();
    }

    @Override
    protected void onMessage(Message msg) {
        if ((msg.mSender == null) && (((Framework.State) msg.mMessage)).equals(Framework.State.AgentsRunning)) {
            // all agents are constructed and registered, go

            // register for the clock ticking, that will drive the
            // time of the simulation
            send(new Message(this, Clock.class, 0, "subscribe"));
        }
        else if (msg.mSender instanceof Clock) {
            tick(Clock.getTime());
        }
    }

    private void tick(long time) {
//        PhysicalPoint.step();
//        Guide.step();
        ObjectOnGuide.tick();

        Main.repaint();
    }

    public RootNode getRoot() {
        return root;
    }

    private void loadWorld() {

        loadPhysicalPoints();
        loadGuides();

        createVehicles();

        connectGuides();

//        Log.d(TAG, String.format("Loaded network: %d messages, %d arcs, %d WordNodes",
//                users.getOutgoing().size(),
//                Arc.getCount(),
//                WordNode.getCount()));

        //          dump(users);

    }

    /**
     now that all the guides are loaded, make connections between
     Guide.to and Guide.from if they are the same physical
     */
    private void connectGuides() {

    }

    private void loadPhysicalPoints() {
        try {
            DB.getDB().getPhysicalPoints (new DB.constructfromDB1() {
                @Override
                public void construct(ResultSet rs) throws SQLException {
                    new PhysicalPoint(getRoot(), rs);
                }
            });
//            for (DBMessage m : v)
//                addToNetwork(m);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void loadGuides() {
        try {
            DB.getDB().getGuides (new DB.constructfromDB1() {
                @Override
                public void construct(ResultSet rs) throws SQLException {
                    new Guide(getRoot(), rs);
                }
            });
//            for (DBMessage m : v)
//                addToNetwork(m);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createVehicles() {

        for (int i = 1; i < Guide.getKnownGuides().size()-1; ++i) {

            Guide guide = Guide.get(i);
            if (i == 5) {
                CompositeVehicle vehicle = new CompositeVehicle(Simulation.this, getRoot(),
                        i,
                        Constants.random.nextDouble(),
                        Constants.random.nextDouble() * 10.0,
                        10.0,
                        30.0,
                        new Route(Simulation.this, guide));
            }
            else {
                Vehicle vehicle = new Vehicle(Simulation.this, getRoot(),
                        i,
                        Constants.random.nextDouble(),
                        Constants.random.nextDouble() * 14.0,
                        14.0,
                        new Route(Simulation.this, guide),
                        new VehicleController());
            }
        }

//        try {
//            DB.getDB().getVehicles (new DB.constructfromDB1() {
//                @Override
//                public void construct(ResultSet rs) throws SQLException {
//                    Guide guide = Guide.get(rs.getLong(2));
//                    if (rs.getInt(6) > Trailer.Length)
//                        new CompositeVehicle(Simulation.this,
//                                getRoot(),
//                                new Route(Simulation.this, guide),
//                                rs);
//                    else
//                        new Vehicle(Simulation.this, getRoot(), new Route(Simulation.this, guide), rs);
//                }
//            });
////            for (DBMessage m : v)
////                addToNetwork(m);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

//    public VehicleAgent getVehicleAgent(String name) {
//        Agent agent = mFramework.findByName(name);
//        assert agent instanceof VehicleAgent;
//
//        return (VehicleAgent) agent;
//    }
}
