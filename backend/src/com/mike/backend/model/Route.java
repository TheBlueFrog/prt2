package com.mike.backend.model;

import com.mike.backend.Simulation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 10/20/2016.
 */
public class Route {

    private List<Guide> guides;
    private int current;

    /**
     * buld a new route for a vehicle
     *
     * @param simulation
     * @param guide         starting Guide
     */
    public Route (Simulation simulation, Guide guide) {
        guides = new ArrayList<Guide>();

        guides.add(guide);
        current = 0;
    }

    public Guide nextGuide(Guide guide) {
        if (++current < guides.size())
            return guides.get(current);
        else
            return null;
    }

    /**
     * the vehicle has arrived at the end of the last guide,
     * dispose of it
     *
     * @param vc
     */
    public void end(AbstractVehicleController vc) {
        Trailer trailer = vc.getLeadVehicle();
        vc.dispose();
        trailer.dispose();
    }

    public Guide getCurrentGuide() {
        return guides.get(current);
    }
}
