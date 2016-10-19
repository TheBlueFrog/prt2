package com.mike.backend.model;

import com.mike.backend.ComposedVehicle;
import com.mike.backend.Simulation;
import com.mike.backend.db.Node;
import com.mike.util.Location;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 Created by mike on 10/12/2016.

 always on a guide, some fraction of the distance between
 guide.from and guide.to, in the range [0..1]

 heading, velocity and fixed length of 2 meters

 bigger vehicles are a chain a basic vehicles, see LongVehicle class
 */

public class Vehicle
        extends ComposedVehicle {

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
        super(simulation,
                parent,
                rs.getLong(1),
                Guide.get(rs.getLong(2)),
                rs.getDouble((3)),
                rs.getDouble(4),
                new VehicleController());
    }

    public Vehicle(Simulation simulation, Node parent,
                   long id,
                   Guide guide,
                   double guideDistance,
                   double velocity,
                   AbstractVehicleController controller) throws SQLException {
        super(simulation,
                parent,
                id,
                guide,
                guideDistance,
                velocity,
                controller);
    }


    @Override
    public String getTag() {
        return Vehicle.class.getSimpleName();
    }

    @Override
    public String toString() {
        return String.format("{%s: on %s, %.2f}", getTag(), guide.toString(), guideDistance);
    }

    public void slowDown() {
        this.slowing = true;
        this.velocity *= 0.9;
    }

    public void adjustVelocityUpTowardsLimit() {
        this.slowing = false;
        this.velocity = Math.min(guide.getMaxVelocity(), velocity * 1.05);
    }

    public Color getColor(ObjectOnGuide vehicle) {
        return ((Vehicle) vehicle).slowing ? Color.red : Color.blue;
    }

    public String getLabel(ObjectOnGuide oog) {
        Vehicle vehicle = (Vehicle) oog;
        if (vehicle.getClosestVehicle() != null)
            return String.format("%d: %d %.1f",
                    vehicle.id,
                    vehicle.getClosestVehicle().getID(),
                    vehicle.getDistanceToClosestVehicle());
        else
            return String.format("%d", vehicle.id);
    }

}
