package com.davidflex.supermarket.ontologies.company.predicates;

import com.davidflex.supermarket.ontologies.ecommerce.concepts.Location;
import jade.content.Predicate;
import jade.core.AID;

/**
 * Used by drones to communicate their position to shop agent.
 */
@SuppressWarnings("unused")
public class InformPosition implements Predicate{

    private AID drone;
    private Location position;

    public InformPosition() {
    }

    public InformPosition(AID drone, Location position) {
        this.drone = drone;
        this.position = position;
    }

    public AID getDrone() {
        return drone;
    }

    public void setDrone(AID drone) {
        this.drone = drone;
    }

    public Location getPosition() {
        return position;
    }

    public void setPosition(Location position) {
        this.position = position;
    }
}
