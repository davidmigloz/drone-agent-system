package com.davidflex.supermarket.ontologies.company.elements;

import jade.content.Concept;
import jade.core.AID;

/**
 * Warehouse.
 */
@SuppressWarnings("unused")
public class Warehouse implements Concept {

    private AID warehouseAgent;
    private Position location;

    public Warehouse() {
    }

    public Warehouse(AID warehouseAgent, Position location) {
        this.warehouseAgent = warehouseAgent;
        this.location = location;
    }

    public AID getWarehouseAgent() {
        return warehouseAgent;
    }

    public void setWarehouseAgent(AID warehouseAgent) {
        this.warehouseAgent = warehouseAgent;
    }

    public Position getLocation() {
        return location;
    }

    public void setLocation(Position location) {
        this.location = location;
    }
}
