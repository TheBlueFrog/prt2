package com.mike.backend;

import com.mike.agents.Agent;
import com.mike.agents.Clock;
import com.mike.agents.Framework;
import com.mike.agents.Message;
import com.mike.backend.db.DB;
import com.mike.backend.db.RootNode;
import com.mike.backend.model.*;
import com.mike.util.Log;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

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

        if (ObjectOnGuide.getKnownVehicles().size() == 0) {
            createVehicles();
        }
    }

    public RootNode getRoot() {
        return root;
    }

    private void loadWorld() {

        loadPhysicalPoints();
        loadGuides();

//        Log.d(TAG, String.format("Loaded network: %d messages, %d arcs, %d WordNodes",
//                users.getOutgoing().size(),
//                Arc.getCount(),
//                WordNode.getCount()));

        //          dump(users);

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

        // setup four 3-guide routes

        List<List<Guide>> guides = new ArrayList<>();
        {
            List<Guide> g = new ArrayList<Guide>();
            g.add(Guide.get(1));
            g.add(Guide.get(2));
            g.add(Guide.get(3));
            guides.add(g);
        }
        {
            List<Guide> g = new ArrayList<Guide>();
            g.add(Guide.get(4));
            g.add(Guide.get(5));
            g.add(Guide.get(6));
            guides.add(g);
        }
        {
            List<Guide> g = new ArrayList<Guide>();
            g.add(Guide.get(7));
            g.add(Guide.get(8));
            g.add(Guide.get(9));
            guides.add(g);
        }
        {
            List<Guide> g = new ArrayList<Guide>();
            g.add(Guide.get(10));
            g.add(Guide.get(11));
            g.add(Guide.get(12));
            guides.add(g);
        }

        int numVehicles = 10;
        double maxVelocity = 14.0;
        double maxTruckVelocity = 9.0;

        for (int i = 1; i < numVehicles; ++i) {

            // uniformly distributed across the guides
            List<Guide> g = guides.get(Constants.random.nextInt(4));

            if (i == 5) {
                new CompositeVehicle(Simulation.this, getRoot(),
                        i,
                        Constants.random.nextDouble() / 2.0,
                        Constants.random.nextDouble() * 10.0,
                        maxTruckVelocity,
                        30.0,
                        new Route(Simulation.this, g));
            }
            else {
                new Vehicle(Simulation.this, getRoot(),
                        i,
                        Constants.random.nextDouble() / 2.0,    // uniformly somewhere in first 1/2 of first guide
                        getVelocity(maxVelocity),   //
                        maxVelocity,
                        new Route(Simulation.this, g),
                        new VehicleController());
            }
        }
    }

    private double getVelocity(double maxVelocity) {
        return Math.max(1.0,
                        Constants.random.nextGaussian() * (maxVelocity * 0.3)    // Gaussian around 0
                        + (maxVelocity * 0.8));      // some a bit over, most under limit
    }

//    public VehicleAgent getVehicleAgent(String name) {
//        Agent agent = mFramework.findByName(name);
//        assert agent instanceof VehicleAgent;
//
//        return (VehicleAgent) agent;
//    }
}
