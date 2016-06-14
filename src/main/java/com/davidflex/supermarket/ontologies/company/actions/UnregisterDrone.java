package com.davidflex.supermarket.ontologies.company.actions;

import jade.content.AgentAction;
import jade.core.AID;

/**
 * Unregister a drone from shopAgent to not appear anymore in the map.
 * And set the order as completed.
 */
@SuppressWarnings("unused")
public class UnregisterDrone implements AgentAction {

    private AID drone;
    private Long orderId;

    public UnregisterDrone() {
    }

    public UnregisterDrone(AID drone, Long orderId) {
        this.drone = drone;
        this.orderId = orderId;
    }

    public AID getDrone() {
        return drone;
    }

    public void setDrone(AID drone) {
        this.drone = drone;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
