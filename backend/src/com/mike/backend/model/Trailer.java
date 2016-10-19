package com.mike.backend.model;

import com.mike.backend.Simulation;
import com.mike.backend.db.Node;

import java.awt.*;
import java.sql.SQLException;

/**
 * Created by mike on 10/18/2016.
 *
 * Trailer objects always have a real Vehicle
 * somewhere ahead of them
 */
public class Trailer extends ObjectOnGuide {

    // trailers are all this long (m), a longer vehicle will
    // have enough trailes to make up the desired length.

    public static double Length = 3.0;

    @Override
    public String getTag() { return Trailer.class.getSimpleName(); }

    protected AbstractVehicleController controller;

    public Trailer(Simulation simulation,
                   Node parent,
                   long id,
                   Guide guide,
                   double guideDistance,
                   double velocity,
                   double maxVelocity,
                   AbstractVehicleController controller) throws SQLException {
        super(parent,
                id,
                guide,
                guideDistance,
                velocity,
                maxVelocity);

        this.controller = controller;
    }

    @Override
    public String getLabel (ObjectOnGuide oog) {
        Trailer vehicle = (Trailer) oog;
        CompositeVehicle mv = (CompositeVehicle) this.getParent();

        if (mv.isLeadVehicle(vehicle)) {
            if (vehicle.getClosestVehicle() != null)
                return String.format("%d: %d %.1f",
                        vehicle.getID(),
                        vehicle.getClosestVehicle().getID(),
                        vehicle.getDistanceToClosestVehicle());
            else
                return String.format("%d", vehicle.getID());
        }
        return null;
    }

    @Override
    public Color getColor(ObjectOnGuide v) {
        CompositeVehicle mv = (CompositeVehicle) this.getParent();
        return mv.getColor();
    }

    /**
     move vehicle one tick
     */
    @Override
    public void ticker () {
        clear();
        controller.tick(this);
    }

    @Override
    public void clear () {
    }

    @Override
    public String toString() {
        return String.format("{%s: on %s, %.2f}",
                getTag(),
                guide.toString(),
                guideDistance);
    }

}
