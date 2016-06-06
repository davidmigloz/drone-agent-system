package com.davidflex.supermarket.ontologies.ecommerce.elements;

import jade.content.Predicate;
import jade.core.AID;

/**
 * The agent contact the shop to start a transaction.
 */
public class ContactRequest implements Predicate {

    private AID buyer;
    private Location location;

    public ContactRequest(AID buyer, Location location) {
        this.buyer = buyer;
        this.location = location;
    }

    public void setBuyer(AID buyer) {
        this.buyer = buyer;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
