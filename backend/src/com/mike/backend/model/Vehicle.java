package com.mike.backend.model;

import com.mike.backend.Constants;
import com.mike.backend.Simulation;
import com.mike.backend.agents.VehicleAgent;
import com.mike.backend.db.Node;
import com.mike.util.Location;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 Created by mike on 10/12/2016.

 always on a guide, some fraction of the distance between
 guide.from and guide.to, in the range [0..1]

 heading, velocity and length
 length is heading + 180 from the location
 */
public class Vehicle extends PhysicalObject {

    static private Map<Long, Vehicle> knownVehicles = new HashMap<>();

    static public Map<Long, Vehicle> getKnownGuides() {
        return knownVehicles;
    }

    public static Vehicle get(long id) {
        return knownVehicles.get(id);
    }

    /**
     each time the simulation clock ticks this will be invoked,
     do the needful
     */
    static public void tick() {
        for (long id : knownVehicles.keySet()) {
            Vehicle vehicle = Vehicle.get(id);
            vehicle.ticker();
        }
    }

    private VehicleController controller;

    private Guide guide;
    private double guideDistance;

    private double heading;
    private double velocity; // in the direction it is heading
    private double length;

    /**
     create from database record

     @param rs
     CREATE TABLE Vehicles (
         id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
         Guide INTEGER,
         GuideDistance DOUBLE,
         Velocity DOUBLE,
         Length DOUBLE,
         ControllerName TEXT

     @throws SQLException
     */
    public Vehicle(Simulation simulation, Node parent, ResultSet rs) throws SQLException {
        super(parent, rs.getLong(1));

        this.guide = Guide.get(rs.getLong(2));
        this.guideDistance = rs.getDouble((3));
        this.velocity = rs.getDouble(4);
        this.length = rs.getDouble(5);

        this.controller = new VehicleController(); //rs.getString(6);

        knownVehicles.put(id, this);
   }

    @Override
    public String getTag() {
        return Vehicle.class.getSimpleName();
    }

    @Override
    public String toString() {
        return String.format("{%s: on %s, %.2f}", getTag(), guide.toString(), guideDistance);
    }

    public static void paint(Graphics2D g2) {

        for (long id : knownVehicles.keySet()) {
            Vehicle vehicle = Vehicle.get(id);

            Location loc = vehicle.getLocation();

            double x = Constants.deg2PixelX(loc.x);
            double y = Constants.deg2PixelY(loc.y);

            Shape s = new Ellipse2D.Double(x, y, 3.0, 3.0);

            Color c = Color.gray;
//        if (task != null) {
//            if (task.isPickup())
            c = Color.MAGENTA;
//            else if (task.isDelivery())
//                c = Color.blue;
////            else
////                c = Color.gray;
//        }

            g2.setColor(c);
            g2.draw(s);
        }
    }

    /**
     the vehicle is along the guide somewhere, that is
     given by the guideDistance fraction

     @return
     */
    private Location getLocation() {
        double dx = guide.getTo().getX() - guide.getFrom().getX();
        double dy = guide.getTo().getY() - guide.getFrom().getY();
        double x = guide.getFrom().getX() + (dx * guideDistance);
        double y = guide.getFrom().getY() + (dy * guideDistance);
        Location loc = new Location(x, y);
        return loc;
    }

    /**
     move vehicle one tick
     */
    public void ticker () {
        controller.tick(this);
    }

    public double getGuideDistance() {
        return guideDistance;
    }

    public void setGuideDistance(double guideDistance) {
        this.guideDistance = guideDistance;
    }
}
