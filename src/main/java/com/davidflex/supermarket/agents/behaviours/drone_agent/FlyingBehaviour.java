package com.davidflex.supermarket.agents.behaviours.drone_agent;

import com.davidflex.supermarket.agents.shop.DroneAgent;
import com.davidflex.supermarket.ontologies.company.concepts.Order;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Location;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;

/**
 * A drone flyes to their target and then comeback to its wharehouse.
 */
public class FlyingBehaviour extends SimpleBehaviour {

    private Location target;
    private int step;

    public FlyingBehaviour(Agent a, Location target) {
        super(a);
        this.target = target;
        step = 0;
    }

    @Override
    public void action() {
        switch (step){
            case 0:
                // Fly to taget
                while(!getActualPosition().equals(target)) {
                    move(target);
                }
                step++;
                break;
            case 1:
                // Deliver order

                break;
            case 2:
                // Wait until the order is delivered
                if(getOrder().isDelivered()) {
                    step++;
                } else {
                    block();
                }
                break;
            case 3:
                // Comeback to warehouse
                while(!getActualPosition().equals(getWarehouseLocation())) {
                    move(getWarehouseLocation());
                }
                step++;
                break;
        }
    }

    @Override
    public boolean done() {
        if(step > 3) {
            // Drone in warehouse -> Unregister from shopAgent
            getAgent().addBehaviour(new UnregisterBehaviour());
            return true;
        }
        return false;
    }


    /**
     * Moves one unit to the target.
     */
    private void move(Location target) {
        int xDir = target.getX() - getActualPosition().getX();
        int yDir = target.getY() - getActualPosition().getX();
        boolean arrived = false;

        // Determine x movement
        if(xDir > 0) {
            // Move down
            getActualPosition().moveDown();
        } else if (xDir < 0) {
            // Move up
            getActualPosition().moveUp();
        }
        // Determine y movement
        if(yDir > 0) {
            // Move right
            getActualPosition().moveRight();
        } else if(yDir < 0) {
            // Move left
            getActualPosition().moveLeft();
        }
    }

    /**
     * Get actual position of the drone.
     */
    private Location getActualPosition() {
        return ((DroneAgent) getAgent()).getPosition();
    }

    /**
     * Get warehouse to which the drone belongs.
     */
    private Location getWarehouseLocation() {
        return ((DroneAgent) getAgent()).getWarehouse().getLocation();
    }

    /**
     * Get order to deliver.
     */
    private Order getOrder() {
        return ((DroneAgent) getAgent()).getOrder();
    }
}
