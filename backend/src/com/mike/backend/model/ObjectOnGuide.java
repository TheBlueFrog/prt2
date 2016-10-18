package com.mike.backend.model;

import com.mike.backend.Constants;
import com.mike.backend.db.Node;
import com.mike.util.Location;
import com.mike.util.Log;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import static com.mike.backend.model.PhysicalPoint.TAG;

/**
 Created by mike on 10/18/2016.

 an object on a guide

 */
public abstract class ObjectOnGuide extends PhysicalObject {

    private static boolean showLabels = true;
    public static boolean getShowLabels() { return showLabels; }
    public static void setShowLabels(boolean show) { showLabels = show; }

    static private Map<Long, ObjectOnGuide> knownVehicles = new HashMap<>();
    static public Map<Long, ObjectOnGuide> getKnownVehicles() {
        return knownVehicles;
    }
    public static ObjectOnGuide get(long id) {
        return knownVehicles.get(id);
    }

    /**
     each time the simulation clock ticks this will be invoked,
     do the needful
     */
    static public void tick() {
        for (long id : knownVehicles.keySet()) {
            ObjectOnGuide vehicle = ObjectOnGuide.get(id);
            vehicle.ticker();
        }
    }

    /**
     move vehicle one tick
     */
    abstract public void ticker ();
    abstract public void clear ();

    /*
    this is what an object on a guide has to keep track of
     */
    protected Guide guide;
    protected double guideDistance;
    protected double velocity; // in the direction it is heading

    /*
    we care about the closest object we could run into, some
    state to track that
     */
    private ObjectOnGuide closestV = null;
    private double closestM = Double.POSITIVE_INFINITY;


    public ObjectOnGuide(Node parent, long id, Guide guide,
                         double guideDistance,
                         double velocity) {
        super(parent, id);

        this.guide = guide;
        this.guideDistance = guideDistance;
        this.velocity = velocity;

        knownVehicles.put(id, this);
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

    /*
    starting a new guide sets the guide distance to zero
     */
    public void setGuide(Guide guide) {
        this.guide = guide;
        guideDistance = 0.0;
    }

    /**
     the vehicle is somewhere along the guide, given by the guideDistance fraction

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
     @return meters to nearest other vehicle, it must be traveling
     in the same direction as we are going
     */
    public void updateClosestVehicle() {
        clearClosest();
        Location myLoc = getLocation();
        for (long id : knownVehicles.keySet())
            if (id != getID()) {
                ObjectOnGuide v = ObjectOnGuide.get(id);
                if (couldHit(v)) {
                    Location otherLoc = v.getLocation();
                    double d = myLoc.distance(otherLoc);

                    if (getGuide().getTo().equals(v.getGuide().getFrom())) {
                        d -= 2;      // worry about the back of the vehicle
                    }

                    if (d < closestM) {
                        closestV = v;
                        closestM = d;
                    }
                }
            }

        if (closestM < 3.0) {
            Log.d(TAG, String.format("Vehicle %d too close to %d, distance %.1f",
                    getID(),
                    closestV.getID(),
                    closestM));
        }
    }

    protected void clearClosest() {
        closestV = null;
        closestM = Double.POSITIVE_INFINITY;
    }

    public double getDistanceToClosestVehicle() {
        return closestM;
    }

    public ObjectOnGuide getClosestVehicle() {
        return closestV;
    }

    /**
     is the vehicle someone we could hit?
     note that if he is on another guide that is not connected
     directly to our guide we can't hit him, e.g. an opposing lane
     or a bridge
     */
    protected boolean couldHit(ObjectOnGuide vehicle) {
        return     (guide.equals(vehicle.getGuide())
                && (getGuideDistance() < vehicle.getGuideDistance()))
                || guide.connectsTo(vehicle.getGuide())
                ;
    }

    public static void paint(Graphics2D g2d) {

        for (long id : knownVehicles.keySet()) {
            ObjectOnGuide vehicle = ObjectOnGuide.get(id);
            vehicle._paint (g2d);
        }
    }

    abstract public String getLabel (ObjectOnGuide v);
    abstract public Color getColor (ObjectOnGuide v);

    public void _paint (Graphics2D g2d) {

        Location loc = getLocation();

        // vehicle is a rectangle with the front at the location
        // and the length trailing along behind it, center it
        // side-to-side on the guide
        //
        // all vehicles are 2 meters wide and 2 meters long

        double x = Constants.deg2PixelX(loc.x);
        double y = Constants.deg2PixelY(loc.y);

        double vehicleWidth = Constants.meter2PixelYScale(1);
        double vehicleLength = Constants.meter2PixelXScale(2);

        //Graphics2D gg = (Graphics2D) g2d.create();
        AffineTransform saveTransform = g2d.getTransform();
        AffineTransform identity = new AffineTransform();
        g2d.setTransform(identity);

        Color c = getColor(this);
        g2d.setColor(c);

        Shape s = new Rectangle2D.Double(-vehicleLength, 0, vehicleLength, vehicleWidth * 2);

        g2d.translate(x, y);

        if (showLabels) {
            String str = getLabel(this);
            if (str != null)
                g2d.drawString(str, 0, 0);
        }

        g2d.rotate(-getGuide().getHeadingR());
        g2d.fill(s);

        g2d.setTransform(saveTransform);
    }

}
