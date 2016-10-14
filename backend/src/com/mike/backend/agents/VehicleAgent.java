package com.mike.backend.agents;

import com.mike.agents.Agent;
import com.mike.agents.Framework;
import com.mike.agents.Message;

/**
 Created by mike on 10/14/2016. */
public class VehicleAgent extends Agent {

    @Override
    protected String getClassName() {
        return VehicleAgent.class.getSimpleName();
    }

    public VehicleAgent(Framework f, Integer serialNumber) {
        super(f, serialNumber);
    }

    @Override
    protected void onMessage(Message msg) {

    }
}
