package com.mike.backend.model;

import com.mike.backend.Constants;
import com.mike.backend.db.Node;
import com.mike.util.Location;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 Created by mike on 10/12/2016.
 */
public class PhysicalPoint extends Node {

    static public String TAG = PhysicalPoint.class.getSimpleName();

    static private Map<Long, PhysicalPoint> knownPoints = new HashMap<>();

    static public Map<Long, PhysicalPoint> getKnownPoints() {
        return knownPoints;
    }

    public static PhysicalPoint get(long id) {
        return knownPoints.get(id);
    }


    private Location location;

    public PhysicalPoint(Node parent, ResultSet rs) throws SQLException {
        super (parent, rs.getLong(1));
        this.location = new Location(rs.getDouble(2), rs.getDouble(3));

        knownPoints.put(id, this);
    }

    public void add(PhysicalObject object) {
        children.add(object);
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String getTag() {
        return PhysicalPoint.class.getSimpleName();
    }

    @Override
    public String toString() {
        return String.format("{%s: %s, %d children}", getTag(), location.toString(), children.size());
    }

    public static void paint(Graphics2D g2) {

        for (long id : knownPoints.keySet()) {
            PhysicalPoint point = knownPoints.get(id);
            Shape circle = null;
            double x = Constants.deg2PixelX(point.getLocation().x);
            double y = Constants.deg2PixelY(point.getLocation().y);

            circle = new Ellipse2D.Double(x-1, y-1, 2.0f, 2.0f);

            Color c = Color.blue;

            g2.setColor(c);
            g2.draw(circle);
        }
    }
}
