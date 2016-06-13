package com.davidflex.supermarket.ontologies.ecommerce.predicates;

import jade.content.Predicate;

/**
 * Response from PersonalAgent to DroneAgent after receiving DeliverRequest.
 * At the moment, we the drone receives it, the deliver is fulfill and it
 * comeback to the warehouse.
 * In the future, in this response could be information about how to perform
 * the deliver. i.e: leave the order in the backyard, etc.
 */
@SuppressWarnings("unused")
public class DeliverResponse implements Predicate{

    public DeliverResponse() {
    }
}
