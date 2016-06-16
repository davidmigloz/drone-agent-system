package com.davidflex.supermarket.agents.behaviours.drone_agent;

import com.davidflex.supermarket.agents.shop.DroneAgent;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Location;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A drone flies to their target.
 * Used by droneAgent.
 */
class FlyingBehaviour extends TickerBehaviour {

    private static final Logger logger = LoggerFactory.getLogger(TickerBehaviour.class);
    private static final int SPEED = 300;

    private Behaviour manager;
    private Location target;

    FlyingBehaviour(Agent a, Behaviour manager, Location target) {
        super(a, SPEED);
        this.manager = manager;
        this.target = target;
    }

    @Override
    public void onTick() {
        if(!getActualPosition().equals(target)) {
            move(target);
        } else {
            // Arrived to target!
            logger.debug("Arrived to target!");
            manager.restart();
            getAgent().removeBehaviour(this);
        }
    }

    /**
     * Moves one unit to the target.
     */
    private void move(Location target) {
        int xDir = target.getX() - getActualPosition().getX();
        int yDir = target.getY() - getActualPosition().getX();

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
        //logger.info(getAgent().getLocalName() + " | Position: " + getActualPosition());
    }

    /**
     * Get actual position of the drone.
     */
    private Location getActualPosition() {
        return ((DroneAgent) getAgent()).getPosition();
    }
}
