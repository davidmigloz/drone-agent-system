package com.davidflex.supermarket.ontologies.ecommerce.predicates;

import jade.content.Predicate;

/**
 * Error while purchase process.
 */
public class PurchaseError implements Predicate {

    private String message;

    public PurchaseError() {
    }

    public PurchaseError(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
