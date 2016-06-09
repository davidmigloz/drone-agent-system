package com.davidflex.supermarket.ontologies.company.elements;

import jade.content.Concept;
import jade.core.AID;

/**
 * Warehouse.
 */
@SuppressWarnings("unused")
public class Warehouse implements Concept {

    private AID warehouseAgent;
    private int x;
    private int y;

    public Warehouse() {
    }

    public Warehouse(AID warehouseAgent, int x, int y) {
        this.warehouseAgent = warehouseAgent;
        this.x = x;
        this.y = y;
    }

    public AID getWarehouseAgent() {
        return warehouseAgent;
    }

    public void setWarehouseAgent(AID warehouseAgent) {
        this.warehouseAgent = warehouseAgent;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
