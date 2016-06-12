package com.davidflex.supermarket.ontologies.company.elements;

import jade.content.AgentAction;
import jade.core.AID;

/**
 * Unregister a drone from shopAgent to not appear anymore in the map.
 */
public class UnregisterDrone implements AgentAction {

    private AID drone;

    public UnregisterDrone() {
    }

    public UnregisterDrone(AID drone) {
        this.drone = drone;
    }

    public AID getDrone() {
        return drone;
    }

    public void setDrone(AID drone) {
        this.drone = drone;
    }
}
