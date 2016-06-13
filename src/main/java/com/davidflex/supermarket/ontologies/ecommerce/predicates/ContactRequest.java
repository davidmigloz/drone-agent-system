package com.davidflex.supermarket.ontologies.ecommerce.predicates;

import com.davidflex.supermarket.ontologies.ecommerce.concepts.Location;
import jade.content.Predicate;
import jade.core.AID;

/**
 * The agent contact the shop to start a transaction.
 */
@SuppressWarnings("unused")
public class ContactRequest implements Predicate {

    private AID buyer;
    private Location location;

    public ContactRequest() {
    }

    public ContactRequest(AID buyer, Location location) {
        this.buyer = buyer;
        this.location = location;
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
}
