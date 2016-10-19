package com.mike.backend.model;

/**
 Created by mike on 10/14/2016.
 */
abstract public class AbstractVehicleController {

    abstract public String getTag();

    /**
    called once per simulation clock tick
     */
    abstract public void tick(Trailer vehicle);

    /**
     see if there is any need to change velocity
     */
    abstract public void adjustVelocity(Vehicle vehicle);

    /**
    something is distance (m) far away is this too close for the given
    vehicle velocity.  it has already been settled that the vehicle
    could collide with the thing.
     */
    abstract public boolean tooCloseForVelocity(double distance, double velocity);

}
