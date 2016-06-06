package com.davidflex.supermarket.ontologies.ecommerce.elements;

import jade.content.Predicate;
import jade.core.AID;

/**
 * Response from shop to a ContactRequest.
 */
public class ContactResponse implements Predicate {

    private AID personalShopAgent;

    public ContactResponse(AID personalShopAgent) {
        this.personalShopAgent = personalShopAgent;
    }

    public AID getPersonalShopAgent() {
        return personalShopAgent;
    }
}
