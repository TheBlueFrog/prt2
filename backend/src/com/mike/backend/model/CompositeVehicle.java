package com.mike.backend.model;

import com.mike.backend.Simulation;
import com.mike.backend.db.Node;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 Created by mike on 10/17/2016.

 a CompositeVehicle is a collection of Vehicles/Trailers operated as a
 single unit
 */
public class CompositeVehicle
        extends PhysicalObject
{
    static private Map<Long, CompositeVehicle> knownMultiVehicles = new HashMap<>();
    private CompositeVehicleController controller;

    static public Map<Long, CompositeVehicle> getKnownMultiVehicles() {
        return knownMultiVehicles;
    }

    public static CompositeVehicle get(long id) {
        return knownMultiVehicles.get(id);
    }

    static public boolean isMultiVehicle(long id) {
        return (id >= 10000);
    }

    protected double length;
    protected List<Trailer> vehicles = new ArrayList<>();


    public CompositeVehicle(Simulation simulation, Node parent, ResultSet rs) throws SQLException {

        super(parent, rs.getLong(1));

        controller = new CompositeVehicleController(this);

        length = rs.getInt(6);

        double guideDistance = rs.getDouble(3);
        Guide guide = Guide.get(rs.getLong(2));

        // lead is a full Vehicle
        vehicles.add(new Vehicle(simulation,
                this,
                id * 10000,
                guide,
                guideDistance,
                rs.getDouble(4),                // velocity
                rs.getDouble(5),                // maxVelocity
                controller));

        // the rest are Trailers
        int k = 1;
        double i = Trailer.Length;
        while (i < length) {
            vehicles.add(new Trailer(simulation, this,
                    id * 10000 + k++,
                    guide,
                    guideDistance,
                    rs.getDouble(4),            // velocity
                    rs.getDouble(5),            // maxVelocity
                    controller));

            // each unit is back down the guide by 2 meters

            double curDistanceM = guideDistance * guide.getLength();
            curDistanceM -= 2;
            guideDistance = curDistanceM / guide.getLength();

            i += Trailer.Length;
        }

        knownMultiVehicles.put(id, this);
    }

    @Override
    public String getTag() { return CompositeVehicle.class.getSimpleName(); }

    @Override
    public String toString() {
        return "";
    }

    public boolean isLeadVehicle(ObjectOnGuide vehicle) {
        return vehicle.getID() == vehicles.get(0).getID();
    }

    public Vehicle getLeadVehicle() {
        assert vehicles.get(0) instanceof Vehicle;
        return (Vehicle) vehicles.get(0);
    }

    public Color getColor() {
        return getLeadVehicle().getColor(null);
    }
}