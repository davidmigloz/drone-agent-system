package com.davidflex.supermarket.ontologies.company.elements;

import jade.content.Predicate;

/**
 * Assign order to personalShopAgent.
 * Used by ShopAgent.
 */
public class AssignOrder implements Predicate {
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
