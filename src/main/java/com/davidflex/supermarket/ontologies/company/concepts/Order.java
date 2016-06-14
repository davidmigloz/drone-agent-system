package com.davidflex.supermarket.ontologies.company.concepts;

import com.davidflex.supermarket.ontologies.ecommerce.concepts.Item;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Location;
import jade.content.Concept;
import jade.core.AID;

import java.util.List;

public class Order implements Concept {
    private long id;
    private AID buyer;
    private Location location;
    private List<Item> items;
    private boolean delivered;

    public Order() {
        this.delivered = false;
    }

    public Order(long id, AID buyer, Location location) {
        this.id = id;
        this.buyer = buyer;
        this.location = location;
        this.delivered = false;
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

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }
}
