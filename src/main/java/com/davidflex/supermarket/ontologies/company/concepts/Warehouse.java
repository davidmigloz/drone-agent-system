package com.davidflex.supermarket.ontologies.company.concepts;

import com.davidflex.supermarket.ontologies.ecommerce.concepts.Location;
import jade.content.Concept;
import jade.core.AID;

/**
 * Warehouse.
 */
@SuppressWarnings("unused")
public class Warehouse implements Concept {

    private AID warehouseAgent;
    private Location location;

    public Warehouse() {
    }

    public Warehouse(AID warehouseAgent, Location location) {
        this.warehouseAgent = warehouseAgent;
        this.location = location;
    }

    public AID getWarehouseAgent() {
        return warehouseAgent;
    }

    public void setWarehouseAgent(AID warehouseAgent) {
        this.warehouseAgent = warehouseAgent;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
