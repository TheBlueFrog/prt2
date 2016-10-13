package com.mike.backend.model;

import com.mike.backend.db.Node;
import com.mike.util.Location;

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

    @Override
    public String getTag() {
        return PhysicalPoint.class.getSimpleName();
    }

    @Override
    public String toString() {
        return String.format("{%s: %s, %d children}", getTag(), location.toString(), children.size());
    }
}
