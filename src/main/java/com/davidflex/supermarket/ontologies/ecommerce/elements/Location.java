package com.davidflex.supermarket.ontologies.ecommerce.elements;

import jade.content.Concept;

/**
 * Location of the buyer.
 */
public class Location implements Concept {

    private int x;
    private int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
