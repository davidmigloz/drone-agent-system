package com.davidflex.supermarket.ontologies.company.predicates;

import com.davidflex.supermarket.ontologies.company.concepts.Order;
import com.davidflex.supermarket.ontologies.company.concepts.Warehouse;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Item;
import jade.content.Predicate;

import java.util.List;

/**
 * Sent to a Warehouse to confirm the purchase for an order.
 * This will start the actual purchase and sending process of the requested items.
 * The purchased items are from the order but can be only a part of them since
 * a same order can be handled by several warehouses.
 *
 * @since   June 11, 2016
 * @author  Constantin MASSON
 */
@SuppressWarnings("unused")
public class ConfirmPurchaseRequest implements Predicate{
    private Order order; //The original order
    private List<Item> items; //List of item to actually buy (With its quantity)
    private Warehouse warehouse; //Warehouse in charge of this sub-order

    public ConfirmPurchaseRequest() {
    }

    public ConfirmPurchaseRequest(Order order, List<Item> items, Warehouse warehouse){
        this.order      = order;
        this.items      = items;
        this.warehouse  = warehouse;
    }


    // *************************************************************************
    // Getters - Setters
    // *************************************************************************
    public Order getOrder() {
        return order;
    }

    public List<Item> getItems() {
        return items;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
}
