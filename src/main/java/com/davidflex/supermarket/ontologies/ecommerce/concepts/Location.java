package com.davidflex.supermarket.ontologies.ecommerce.concepts;

import jade.content.Concept;

/**
 * Location of the buyer.
 */
@SuppressWarnings("unused")
public class Location implements Concept {

    private int x;
    private int y;

    public Location() {
    }

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
