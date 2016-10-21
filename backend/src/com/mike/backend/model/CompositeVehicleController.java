package com.mike.backend.model;

import com.mike.backend.Constants;
import com.mike.backend.agents.MyClock;

import java.util.List;

/**
 Created by mike on 10/14/2016.
 */
public class CompositeVehicleController extends AbstractVehicleController {

    @Override
    public String getTag() { return CompositeVehicleController.class.getSimpleName(); }

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
            velocityMPS = getLeadVehicle().getVelocity();
        }

        // convert velocity m/s into fraction of guide length
        double guideLengthM = vehicle.getGuide().getLength();
        double curDistanceM = vehicle.getGuideDistance() * guideLengthM;

        curDistanceM += (velocityMPS * (MyClock.msecondsPerSimulationTick / 1000.0));

        double newDistance = curDistanceM / guideLengthM;

        if (newDistance < 1.0) {
            vehicle.setGuideDistance(newDistance);
        } else {
//            if (vehicle instanceof Vehicle) {

                vehicle.atEndOfGuide(this);
//            }
//            else {
//                // end of the current guide follow the
//                // guy leading this chain of Trailers
//
//                // @ToDo // FIXME: 10/18/2016
//                // hmm, this will break if the chain is longer
//                // than a guide..
//
//                Vehicle leadVehicle = mv.getLeadVehicle();
//                vehicle.setGuide(leadVehicle.getGuide());
//            }
        }
    }

    public void adjustVelocity(Vehicle vehicle) {
        if (mv.isLeadVehicle(vehicle)) {
            vehicle.updateClosestVehicle();
            double closestVehicleM = vehicle.getDistanceToClosestVehicle();

            if (tooCloseForVelocity(closestVehicleM, vehicle.getVelocity())) {
                vehicle.slowDown();
            } else {
                vehicle.accelerate();
            }
        }
    }

    public boolean tooCloseForVelocity(double distance, double velocity) {
        return distance < (velocity * 5.0);
    }

    @Override
    public Trailer getLeadVehicle() {
        return mv.getLeadVehicle();
    }

    @Override
    void dispose() {
        mv.dispose();
    }

}
