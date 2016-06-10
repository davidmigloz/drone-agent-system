package com.davidflex.supermarket.ontologies.company.elements;

import jade.content.Predicate;
import jade.core.AID;

/**
 * Used by drones to communicate their position to shop agent.
 */
@SuppressWarnings("unused")
public class InformPosition implements Predicate{

    private AID drone;
    private Position position;

    public InformPosition() {
    }

    public InformPosition(AID drone, Position position) {
        this.drone = drone;
        this.position = position;
    }

    public AID getDrone() {
        return drone;
    }

    public void setDrone(AID drone) {
        this.drone = drone;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
