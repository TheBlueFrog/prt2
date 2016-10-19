package com.mike.backend;

import com.mike.backend.db.Node;
import com.mike.backend.model.*;

import java.awt.*;
import java.sql.SQLException;

/**
 * Created by mike on 10/18/2016.
 *
 * ComposedVehicle objects always have a real Vehicle
 * somewhere ahead of them
 */
public class ComposedVehicle extends ObjectOnGuide {

    @Override
    public String getTag() { return ComposedVehicle.class.getSimpleName(); }

    protected AbstractVehicleController controller;
    protected boolean slowing = false;

    public ComposedVehicle(Simulation simulation,
                           Node parent,
                           long id,
                           Guide guide,
                           double guideDistance,
                           double velocity,
                           AbstractVehicleController controller) throws SQLException {
        super(parent,
            id,
            guide,
            guideDistance,
            velocity);

        this.controller = controller;
    }

    @Override
    public String getLabel (ObjectOnGuide oog) {
        ComposedVehicle vehicle = (ComposedVehicle) oog;
        LongVehicle mv = (LongVehicle) this.getParent();

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
        return null;
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
        slowing = false;
    }

    @Override
    public String toString() {
        return null;
    }
}
