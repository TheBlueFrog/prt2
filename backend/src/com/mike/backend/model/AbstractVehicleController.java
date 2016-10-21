package com.mike.backend.model;

/**
 Created by mike on 10/14/2016.
 */
abstract public class AbstractVehicleController {

    protected Trailer trailer;

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
    is this too close to somebody else given the
    vehicle's velocity.  it has already been settled that the vehicle
    could collide with the thing.
     */
    abstract public boolean tooCloseForVelocity(Vehicle vehicle);

    /**
     * @return the lead Vehicle of what is being controlled
     */
    public abstract Trailer getLeadVehicle();

    public void setVehicle(Trailer trailer) {
        this.trailer = trailer;
    }

    /**
     * dispose of this vehicle
     */
    void dispose() {
//        trailer = null;
    }
}
