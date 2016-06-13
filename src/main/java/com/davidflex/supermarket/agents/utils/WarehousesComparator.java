package com.davidflex.supermarket.agents.utils;

import com.davidflex.supermarket.ontologies.company.concepts.Warehouse;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Location;

import java.util.Comparator;

/**
 * Sort a list of warehouses according to their distance to the user's location.
 */
public class WarehousesComparator implements Comparator<Warehouse> {

    private Location customer;

    public WarehousesComparator(Location customerLocation) {
        this.customer = customerLocation;
    }

    @Override
    public int compare(Warehouse w1, Warehouse w2) {
        double d1 = squaredEuclidianDistance(customer.getX(), customer.getX(),
                w1.getLocation().getX(), w1.getLocation().getY());
        double d2 = squaredEuclidianDistance(customer.getX(), customer.getX(),
                w2.getLocation().getX(), w2.getLocation().getY());
        return (int) (d1 - d2);
    }

    /**
     * Calculate the Squared Euclidean Distance from the point to another point.
     * It place progressively greater weight on objects that are farther apart.
     *
     * @return the square euclidean distance.
     */
    private double squaredEuclidianDistance(int x1, int x2, int y1, int y2) {
        double d = 0D;
        d += Math.pow(x1 - y1, 2.0);
        d += Math.pow(x2 - y2, 2.0);
        return d;
    }
}
