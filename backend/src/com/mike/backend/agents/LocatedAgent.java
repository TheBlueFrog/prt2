package com.mike.backend.agents;

import com.mike.agents.Framework;
import com.mike.util.Location;

/**
 * Created by mike on 6/17/2016.
 */
public abstract class LocatedAgent extends PaintableAgent {

    static private final String TAG = LocatedAgent.class.getSimpleName();

    private Location location;

    @Override
    protected String getClassName() {
        return null;
    }

    public LocatedAgent(Framework f, int serialNumber) {
        super(f, serialNumber);

        location = new Location(0, 0);
    }

//    @Override
//    protected void onMessage(Message msg) {
//    }

    protected Location getLocation () { return new Location(location); }
    protected void setLocation (Location location) { this.location = new Location(location); }

}
