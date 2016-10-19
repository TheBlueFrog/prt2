package com.mike.backend.model;

import com.mike.backend.Constants;
import com.mike.backend.agents.MyClock;
import com.mike.util.Log;

import java.util.List;

/**
 Created by mike on 10/14/2016.
 */
public class VehicleController extends AbstractVehicleController {

    public String getTag () { return VehicleController.class.getSimpleName(); }

    public void tick(Trailer vehicle) {

        assert vehicle instanceof Vehicle;
        _tick((Vehicle) vehicle);
    }

    private void _tick(Vehicle vehicle) {

        adjustVelocity(vehicle);

        double velocityMPS = vehicle.getVelocity();

        // convert velocity m/s into fraction of guide length
        double guideLengthM = vehicle.getGuide().getLength();
        double curDistanceM = vehicle.getGuideDistance() * guideLengthM;

        curDistanceM += (velocityMPS * (MyClock.msecondsPerSimulationTick / 1000.0));

        double newDistance = curDistanceM / guideLengthM;

        if (newDistance < 1.0) {
            vehicle.setGuideDistance(newDistance);
        }
        else {
            // end of the current guide figure out what to do now...
            Guide guide = Guide.getRandom();
            vehicle.setGuide(guide);
        }
    }

    /**
     see if there is any need to change velocity
     */
    public void adjustVelocity(Vehicle vehicle) {
        vehicle.updateClosestVehicle();
        double closestVehicleM = vehicle.getDistanceToClosestVehicle();

        if (tooCloseForVelocity(closestVehicleM, vehicle.getVelocity())) {
            vehicle.slowDown();
        }
        else {
            vehicle.accelerate();
        }


    }

    /**
     for the current velocity of the vehicle is the given distance
     too close? basically 2 seconds
     @param distance
     @return
     */
    public boolean tooCloseForVelocity(double distance, double velocity) {
        return distance < (velocity * 3.0);
    }

}
