package com.davidflex.supermarket.ontologies.ecommerce.predicates;

import jade.content.Predicate;
import jade.core.AID;

/**
 * Response from shop to a ContactRequest.
 */
@SuppressWarnings("unused")
public class ContactResponse implements Predicate {

    private AID personalShopAgent;

    public ContactResponse() {
    }

    public ContactResponse(AID personalShopAgent) {
        this.personalShopAgent = personalShopAgent;
    }

    public AID getPersonalShopAgent() {
        return personalShopAgent;
    }

    public void setPersonalShopAgent(AID personalShopAgent) {
        this.personalShopAgent = personalShopAgent;
    }
}
