package com.davidflex.supermarket.ontologies.company.concepts;

import jade.content.Concept;

/**
 * Position of a drone or warehouse agent.
 */
@SuppressWarnings("unused")
public class Position implements Concept{

    private int x;
    private int y;

    public Position() {
    }

    public Position(int x, int y) {
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

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }
}
