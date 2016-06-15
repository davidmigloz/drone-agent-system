package com.davidflex.supermarket.ontologies.company.actions;

import com.davidflex.supermarket.ontologies.company.concepts.Order;
import jade.content.AgentAction;

/**
 * Assign order to personalShopAgent.
 * Used by ShopAgent and DroneAgent
 */
@SuppressWarnings("unused")
public class AssignOrder implements AgentAction {
    private Order order;

    public AssignOrder() {
    }

    public AssignOrder(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
