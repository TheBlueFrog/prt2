package com.mike.backend.model;

/**
 Created by mike on 10/14/2016. */
public class VehicleController {

    public void tick(Vehicle vehicle) {
        double delta = 0.01;
        vehicle.setGuideDistance(vehicle.getGuideDistance() + delta);

        if (vehicle.getGuideDistance() >= 1.0) {
            // end of the guide figure out what to do now...
            
        }

    }
}
