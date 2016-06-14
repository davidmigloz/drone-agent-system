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

    public void moveUp() {
        this.x--;
    }

    public void moveDown() {
        this.x++;
    }

    public void moveRight() {
        this.y++;
    }

    public void moveLeft() {
        this.y--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return x == location.x && y == location.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "{" + x + ", " + y + '}';
    }
}
