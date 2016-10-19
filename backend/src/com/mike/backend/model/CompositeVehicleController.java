package com.mike.backend.model;

import com.mike.backend.Constants;

import java.util.List;

/**
 Created by mike on 10/14/2016.
 */
public class CompositeVehicleController extends AbstractVehicleController {

    private final CompositeVehicle mv;

    public CompositeVehicleController(CompositeVehicle mv) {
        this.mv = mv;
    }

    public void tick(Trailer vehicle) {

        double velocityMPS;
        if (vehicle instanceof Vehicle) {

            adjustVelocity((Vehicle) vehicle);

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
            if (vehicle instanceof Vehicle) {
                // end of the current guide figure out what to do now...
                Guide cur = vehicle.getGuide();
                List<Guide> next = cur.getNextGuides();
                if (next.size() > 0) {
                    // if choice pick at random
                    int i = Constants.random.nextInt(next.size());
                    vehicle.setGuide(next.get(i));
                }
                else {
                    vehicle.setVelocity(0.0);
                }
            }
            else {
                // end of the current guide follow the
                // guy leading this chain of Trailer

                // @ToDo // FIXME: 10/18/2016
                // hmm, this will break if the chain is longer
                // than a guide..

                Vehicle leadVehicle = mv.getLeadVehicle();
                vehicle.setGuide(leadVehicle.getGuide());
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
