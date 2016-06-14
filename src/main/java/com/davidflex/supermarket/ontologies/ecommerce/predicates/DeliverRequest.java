package com.davidflex.supermarket.ontologies.ecommerce.predicates;

import com.davidflex.supermarket.ontologies.ecommerce.concepts.Item;
import jade.content.Predicate;

import java.util.List;

/**
 * Drones PersonalAgent that it is ready for delivering the order.
 */
public class DeliverRequest implements Predicate{

    private List<Item> items;

    public DeliverRequest() {
    }

    public DeliverRequest(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
