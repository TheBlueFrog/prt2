package com.mike.backend.model;

import com.mike.backend.Constants;
import com.mike.backend.db.Node;
import com.mike.util.Location;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 Created by mike on 10/12/2016.
 A guide is a virtual rail from one PhysicalPoint to another
 */
public class Guide extends PhysicalObject {

    static private Map<Long, Guide> knownGuides = new HashMap<>();
    private double heading;
    private double length;
    private double maxVelocity;

    static public Map<Long, Guide> getKnownGuides() {
        return knownGuides;
    }

    public static Guide get(long id) {
        return knownGuides.get(id);
    }

    @Override
    public String getTag() {
        return Guide.class.getSimpleName();
    }

    @Override
    public String toString() {
        return String.format("{%s: from %s to %s}", getTag(), from.toString(), to.toString());
    }

    private PhysicalPoint from;
    private PhysicalPoint to;
//    private List<Guide> connected = new ArrayList<>();

    public Guide(Node parent, ResultSet rs) throws SQLException {
        super(parent, rs.getLong(1));

        this.from = PhysicalPoint.get(rs.getLong(2));
        this.from.add(this);

        this.to = PhysicalPoint.get(rs.getLong(3));
        this.to.add(this);

        maxVelocity = 13.4; // about 30mph in m/s
        length = from.distance(to);
        heading = Math.atan2(to.getY() - from.getY(), to.getX() - from.getX());

        knownGuides.put(id, this);
    }

    public static void paint(Graphics2D g2) {

        for (long id : knownGuides.keySet()) {
            Guide guide = knownGuides.get(id);

            double x1 = Constants.deg2PixelX(guide.from.getLocation().x);
            double y1 = Constants.deg2PixelY(guide.from.getLocation().y);
            double x2 = Constants.deg2PixelX(guide.to.getLocation().x);
            double y2 = Constants.deg2PixelY(guide.to.getLocation().y);

            Shape s = new Line2D.Double(x1, y1, x2, y2);

        Color c = Color.LIGHT_GRAY;
//        if (task != null) {
//            if (task.isPickup())
//                c = Color.red;
//            else if (task.isDelivery())
//                c = Color.blue;
////            else
////                c = Color.gray;
//        }

            g2.setColor(c);
            g2.draw(s);
        }
    }

    public PhysicalPoint getTo() {
        return to;
    }

    public PhysicalPoint getFrom() {
        return from;
    }

    public List<Guide> getNextGuides() {
        return Guide.nextGuides(to);
    }

    private static List<Guide> nextGuides(PhysicalPoint to) {
        List<Guide> v = new ArrayList<>();
        for (long id : knownGuides.keySet()) {
            Guide guide = knownGuides.get(id);
            if (guide.from.equals(to))
                v.add(guide);
        }
        return v;
    }

    public double getLength() {
        return length;
    }

    /**
     return current heading in radians
     */
    public double getHeadingR() {
        return heading;
    }

    public double getMaxVelocity() {
        return maxVelocity;
    }

    public boolean connectsTo(Guide guide) {
        return     to.equals(guide.from) // normal following case
                || ( ! from.equals(guide.from) && to.equals(guide.to)) // joining guides
                ;
    }
}
