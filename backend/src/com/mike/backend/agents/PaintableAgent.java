package com.mike.backend.agents;

import com.mike.agents.Agent;
import com.mike.agents.Framework;

import java.awt.*;

/**
 * Created by mike on 6/17/2016.
 */
abstract public class PaintableAgent extends Agent {

    static private final String TAG = PaintableAgent.class.getSimpleName();

    @Override
    protected String getClassName() {
        return null;
    }

    /**
     *
     * @param g2    graphics context to use to paint this agent
     */
    abstract void paint(Graphics2D g2);

    public PaintableAgent(Framework f, int serialNumber) {
        super(f, serialNumber);
    }

}
