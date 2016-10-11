package com.mike.agents;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mike on 6/17/2016.
 */
public class AgentInfo {

    Class<? extends Agent> agentClass;
    Map<Integer, Agent> agents;
    int state;
    public int copies = 1;


    /**
     * make an arbitrary number of agents of this class, each
     * has a unique serial number passed to the constructor
     *
     * @param agentClass
     * @param copies
     */
    public AgentInfo(Class<? extends Agent> agentClass, int copies) {
        this.agentClass = agentClass;
        this.agents = new HashMap<Integer, Agent>();
        this.state = 0;
        this.copies = copies;
    }

//	public AgentInfo(Class<? extends Agent> agentClass, Agent agent, int state) {
//        this.agentClass = agentClass;
//        this.agent = agent;
//        this.state = state;
//    }
}
