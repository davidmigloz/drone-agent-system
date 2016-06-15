package com.davidflex.supermarket.ontologies.company.predicates;

import jade.content.Predicate;

/**
 * Response from ConfirmPurchaseRequest.
 * Give the answer if the purchase has been successfully done or not (No drone
 * available)
 *
 * @since   June 15, 2016
 * @author  Constantin MASSON
 */
public class ConfirmPurchaseResponse implements Predicate{
    private boolean isDone;
    private String  errorMessage;

    public ConfirmPurchaseResponse(){
        this.errorMessage = "";
    }

    public boolean isDone() {
        return isDone;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
