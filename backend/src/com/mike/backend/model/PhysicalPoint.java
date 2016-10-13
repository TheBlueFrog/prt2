package com.mike.backend.model;

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
public class PhysicalPoint {

    static public String TAG = PhysicalPoint.class.getSimpleName();
    static private Map<Long, PhysicalPoint> knownPoints = new HashMap<>();

    static public Map<Long, PhysicalPoint> getKnownPoints() {
        return knownPoints;
    }


    private Location location;

    List<PhysicalObject> objectsHere = new ArrayList<>();

    public PhysicalPoint(ResultSet rs) throws SQLException {
        long id = rs.getLong(1);
        this.location = new Location(rs.getDouble(2), rs.getDouble(3));
        knownPoints.put(id, this);
    }

    public void add(PhysicalObject object) {
        objectsHere.add(object);
    }

    public static PhysicalPoint get(long id) {
        return knownPoints.get(id);
    }
}
