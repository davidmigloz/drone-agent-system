package com.davidflex.supermarket.ontologies.company.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;
import com.davidflex.supermarket.ontologies.ecommerce.elements.Location;
import jade.content.Concept;
import jade.core.AID;

import java.util.List;

public class Order implements Concept {
    private long id;
    private AID buyer;
    private Location location;
    private List<Item> items;

    public Order() {
    }

    public Order(long id, AID buyer, Location location) {
        this.id = id;
        this.buyer = buyer;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AID getBuyer() {
        return buyer;
    }

    public void setBuyer(AID buyer) {
        this.buyer = buyer;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
