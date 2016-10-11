package com.mike.backend.agents;

import com.mike.agents.Agent;
import com.mike.agents.Clock;
import com.mike.agents.Framework;
import com.mike.agents.Message;
import com.mike.backend.Main;

/**
 Created by mike on 10/11/2016. */
public class MyClock extends Clock {

    private long secondsPerSimulationTick = 1;
    private boolean animation = false;

    public MyClock(Framework f, Integer serialNumber) {
        super(f, serialNumber);
        animation = Main.isAnimation();
    }

    @Override
    protected void handleTick() {
        // just talking to myself

        time += secondsPerSimulationTick; // each tick moves simulation clock this many seconds

        for (Agent a : subscribers)
            send(new Message(this, a.getClass(), a.getSerialNumber(), (Long) time));

        try {
            sleep(animation ? 1 : 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
