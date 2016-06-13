package com.davidflex.supermarket.ontologies.company.actions;

import com.davidflex.supermarket.ontologies.company.concepts.Warehouse;
import jade.content.AgentAction;

/**
 * Register warehouse in ShopAgent.
 */
@SuppressWarnings("unused")
public class RegisterWarehouse implements AgentAction {

    private Warehouse warehouse;

    public RegisterWarehouse() {
    }

    public RegisterWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
}
