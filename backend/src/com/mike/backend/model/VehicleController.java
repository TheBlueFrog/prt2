package com.mike.backend.model;

import java.util.List;

/**
 Created by mike on 10/14/2016. */
public class VehicleController {

    public void tick(Vehicle vehicle) {
        double distance = vehicle.getGuideDistance();
        double delta = 0.001;

        if (vehicle.getGuideDistance() < 1.0) {
            vehicle.setGuideDistance(distance + delta);
        }
        else {
            // end of the guide figure out what to do now...
            Guide cur = vehicle.getGuide();
            List<Guide> next = cur.getNextGuides();
            if (next.size() > 0) {
                vehicle.setGuide(next.get(0));
            }
            else {
                vehicle.setVelocity(0.0);
            }
        }
    }
}
