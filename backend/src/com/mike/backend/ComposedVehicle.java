package com.mike.backend;

import com.mike.backend.db.Node;
import com.mike.backend.model.*;

import java.sql.SQLException;

/**
 * Created by mike on 10/18/2016.
 */
public class ComposedVehicle extends Vehicle {

    public ComposedVehicle(Simulation simulation,
                           Node parent,
                           long id,
                           Guide guide,
                           double guideDistance,
                           double velocity,
                           MultiVehicleController controller) throws SQLException {
        super(simulation,
            parent,
            id,
            guide,
            guideDistance,
            velocity,
            controller);
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
}
