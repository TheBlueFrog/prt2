package com.mike.backend.model;

import com.mike.backend.Constants;
import com.mike.backend.LongVehicle;

import java.util.List;

/**
 Created by mike on 10/14/2016.
 */
public class MultiVehicleController extends AbstractVehicleController {

    private final LongVehicle mv;

    public MultiVehicleController (LongVehicle mv) {
        this.mv = mv;
    }

    public void tick(Vehicle vehicle) {

        double velocityMPS;
        if (mv.isLeadVehicle(vehicle)) {

            adjustVelocity(vehicle);

            velocityMPS = vehicle.getVelocity();
        }
        else {
            velocityMPS = mv.getLeadVehicle().getVelocity();
        }

        // convert velocity m/s into fraction of guide length
        double guideLengthM = vehicle.getGuide().getLength();
        double curDistanceM = vehicle.getGuideDistance() * guideLengthM;

        curDistanceM += velocityMPS; // assuming a tick is 1 second

        double newDistance = curDistanceM / guideLengthM;

        if (newDistance < 1.0) {
            vehicle.setGuideDistance(newDistance);
        } else {
            // end of the current guide figure out what to do now...
            Guide cur = vehicle.getGuide();
            List<Guide> next = cur.getNextGuides();
            if (next.size() > 0) {
                // if choice pick at random
                int i = Constants.random.nextInt(next.size());
                vehicle.setGuide(next.get(i));
            } else {
                vehicle.setVelocity(0.0);
            }
        }
    }

    public void adjustVelocity(Vehicle vehicle) {
        if (mv.isLeadVehicle(vehicle)) {
            vehicle.updateClosestVehicle();
            double closestVehicleM = vehicle.getDistanceToClosestVehicle();

            if (tooCloseForVelocity(closestVehicleM, vehicle.getVelocity())) {
                vehicle.slowDown();
            } else {
                vehicle.adjustVelocityUpTowardsLimit();
            }
        }
    }

    public boolean tooCloseForVelocity(double distance, double velocity) {
        return distance < (velocity * 5.0);
    }

}
