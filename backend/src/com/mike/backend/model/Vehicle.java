package com.mike.backend.model;

import com.mike.backend.Constants;
import com.mike.backend.Simulation;
import com.mike.backend.db.Node;
import com.mike.util.Location;

import java.awt.*;
import java.awt.geom.Ellipse2D;
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

    private static boolean showLabels = false;

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

    private boolean slowing = false;

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

            Shape s = new Ellipse2D.Double(x - 2, y - 2, 4.0, 4.0);

            Color c = Color.blue;
            if (vehicle.slowing)
                c = Color.red;

            g2.setColor(c);
            g2.draw(s);

            if (showLabels)
                g2.drawString(Long.toString(id), (float) x, (float) (y - 2.0));
        }
    }

    /**
     @return meters to nearest other vehicle, in the direction
     we are going. otherwise the vehicle ahead will slow for
     us as well

     @param vehicle
     */
    public static double closestVehicleM(Vehicle vehicle) {
        double closestM = Double.POSITIVE_INFINITY;
        Location myLoc = vehicle.getLocation();
        for (long id : knownVehicles.keySet())
            if (id != vehicle.getID()) {
                Vehicle v = Vehicle.get(id);
                Location otherLoc = v.getLocation();
                double d = myLoc.distance(otherLoc);
                if (d < closestM)
                    if (vehicle.isAhead(otherLoc)) {
                        closestM = d;
                    }
            }

        return closestM;
    }

    static double R10 = 10.0 * ((2 * Math.PI) / 360.0);

    /**
     is the location ahead of us, we take that to mean within
     +/- 10 deg of our current heading
     */
    private boolean isAhead(Location loc) {
        double ourHeading = guide.getHeadingR();
        Location ourLoc = getLocation();
        double itsHeading = Math.atan2(loc.y - ourLoc.y, loc.x - ourLoc.x);
        return Math.abs(ourHeading - itsHeading) < R10;
    }


    public double distance(Location location) {
        return getLocation().distance(location);
    }

    /**
     the vehicle is along the guide somewhere, that is
     given by the guideDistance fraction

     @return
     */
    public Location getLocation() {
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
        clear();
        controller.tick(this);
    }

    private void clear () {
        slowing = false;
    }

    public double getGuideDistance() {
        return guideDistance;
    }

    public void setGuideDistance(double guideDistance) {
        this.guideDistance = guideDistance;
    }

    public Guide getGuide() {
        return guide;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
        guideDistance = 0.0;
    }

    public void slowDown() {
        this.slowing = true;
        this.velocity *= 0.9;
    }

    public void adjustVelocityUpTowardsLimit() {
        this.slowing = false;
        this.velocity = Math.min(guide.getMaxVelocity(), velocity * 1.05);
    }

    public static void setShowLabels(boolean show) {
        showLabels = show;
    }
}
