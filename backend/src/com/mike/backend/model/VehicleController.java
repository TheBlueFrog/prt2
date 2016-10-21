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

    public VehicleController() {
        super ();
    }
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
            vehicle.atEndOfGuide(this);
//            // at end of guide, see if it has a single outgoing connection
//            Guide cur = vehicle.getGuide();
//            List<Guide> outgoing = cur.getOutgoing();
//            if (outgoing.size() == 1) {
//                // take it
//                vehicle.setGuide(outgoing.get(0));
//            } else {
//                // figure out what to do now...
//                Guide guide = Guide.getRandom();
//                vehicle.setGuide(guide);
//            }
        }
    }

    /**
     see if there is any need to change velocity
     */
    public void adjustVelocity(Vehicle vehicle) {

        vehicle.updateClosestVehicle();

        if (tooCloseForVelocity(vehicle)) {
            vehicle.slowDown();
        }
        else {
            vehicle.accelerate();
        }
    }

    /**
     for the current velocity of the vehicle is the given distance
     too close? basically 2 seconds
     @return
     */
    public boolean tooCloseForVelocity(Vehicle vehicle) {
        if (vehicle.getClosestVehicle() == null)
            return false;

        double distance = vehicle.getDistanceToClosestVehicle();
        return distance < (vehicle.getVelocity() * 3.0);
    }

    @Override
    public Trailer getLeadVehicle() {
        return trailer;
    }

    @Override
    void dispose() {

    }

}
