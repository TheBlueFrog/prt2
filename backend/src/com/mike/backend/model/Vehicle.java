package com.mike.backend.model;

import com.mike.backend.Constants;
import com.mike.backend.Simulation;
import com.mike.backend.db.Node;
import com.mike.util.Location;
import com.mike.util.Log;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.mike.backend.model.PhysicalPoint.TAG;

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

    public static void paint(Graphics2D g2d) {

        for (long id : knownVehicles.keySet()) {
            Vehicle vehicle = Vehicle.get(id);

            Location loc = vehicle.getLocation();

            // vehicle is a rectangle with the front at the location
            // and the length trailing along behind it, center it
            // side-to-side on the guide
            //
            // all vehicles are 2 meters wide

            // @ToDo // FIXME: 10/16/2016 there is an artifact at a corner
            // of the vehicle sticking out backwards along the new guide...

            double x = Constants.deg2PixelX(loc.x);
            double y = Constants.deg2PixelY(loc.y);
            double vehicleWidth = Constants.deg2PixelYScale(1);
            double vehicleLength = Constants.deg2PixelXScale(vehicle.length);

            //Graphics2D gg = (Graphics2D) g2d.create();
            AffineTransform saveTransform = g2d.getTransform();
            AffineTransform identity = new AffineTransform();
            g2d.setTransform(identity);

            Color c = vehicle.slowing ? Color.red : Color.blue;
            g2d.setColor(c);

            Shape s = new Rectangle2D.Double(- vehicleLength, 0, vehicleLength, vehicleWidth*2);

            g2d.translate(x, y);

            if (showLabels)
                g2d.drawString(Long.toString(id), (float) 0, (float) 0);

            g2d.rotate(- vehicle.getGuide().getHeadingR());
            g2d.fill(s);

            g2d.setTransform(saveTransform);
        }
    }

    /**
     @return meters to nearest other vehicle, in the direction
     we are going. otherwise the vehicle ahead will slow for
     us as well

     @param vehicle
     */
    public static double closestVehicleM(Vehicle vehicle) {
        Vehicle closestV = null;
        double closestM = Double.POSITIVE_INFINITY;
        Location myLoc = vehicle.getLocation();
        for (long id : knownVehicles.keySet())
            if (id != vehicle.getID()) {
                Vehicle v = Vehicle.get(id);
                Location otherLoc = v.getLocation();
                double d = myLoc.distance(otherLoc);
                if (vehicle.couldHit(v)) {
                    d -= v.length;      // worry about his length
                    if (d < closestM) {
                        closestV = v;
                        closestM = d;
                    }
                }
            }

        if (closestM < 3.0) {
            Log.d(TAG, String.format("Vehicle %d too close to %d", vehicle.getID(), closestV.getID()));
        }

        return closestM;
    }

    static double R10 = 10.0 * ((2 * Math.PI) / 360.0);

    /**
     is the vehicle someone we could hit?
     note that if he is on another guide that is not connected
     directly to our guide we don't care
     */
    private boolean couldHit(Vehicle vehicle) {
        return     guide.equals(vehicle.getGuide())
                || guide.connectsTo(vehicle.getGuide())
                ;
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
