package com.mike.backend;

import com.mike.backend.db.Node;
import com.mike.backend.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 Created by mike on 10/17/2016.

 a LongVehicle is a collection of Vehicles operated as a
 single unit
 */
public class LongVehicle
        extends PhysicalObject
{
    static private Map<Long, LongVehicle> knownMultiVehicles = new HashMap<>();
    private MultiVehicleController controller;

    static public Map<Long, LongVehicle> getKnownMultiVehicles() {
        return knownMultiVehicles;
    }

    public static LongVehicle get(long id) {
        return knownMultiVehicles.get(id);
    }

    static public boolean isMultiVehicle(long id) {
        return (id >= 10000);
    }

    protected int numUnits;
    protected List<ComposedVehicle> vehicles = new ArrayList<>();


    public LongVehicle(Simulation simulation, Node parent, ResultSet rs) throws SQLException {

        super(parent, rs.getLong(1));

        controller = new MultiVehicleController(this);

        numUnits = rs.getInt(5);

        double guideDistance = rs.getDouble(3);
        Guide guide = Guide.get(rs.getLong(2));

        for (int i = 0; i < numUnits; ++i) {
            vehicles.add(new ComposedVehicle(simulation, this,
                    id * 10000 + i,
                    guide,
                    guideDistance,
                    rs.getDouble(4),             // velocity
                    controller));

            // each unit is back down the guide by 2 meters

            double curDistanceM = guideDistance * guide.getLength();
            curDistanceM -= 2;
            guideDistance = curDistanceM / guide.getLength();
        }

        knownMultiVehicles.put(id, this);
    }

    @Override
    public String getTag() { return LongVehicle.class.getSimpleName(); }

    @Override
    public String toString() {
        return "";
    }

    public boolean isLeadVehicle(ObjectOnGuide vehicle) {
        return vehicle.getID() == vehicles.get(0).getID();
    }

    public Vehicle getLeadVehicle() {
        return vehicles.get(0);
    }



}
