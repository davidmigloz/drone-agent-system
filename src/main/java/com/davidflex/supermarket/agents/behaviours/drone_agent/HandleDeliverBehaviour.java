package com.davidflex.supermarket.agents.behaviours.drone_agent;

import com.davidflex.supermarket.agents.shop.DroneAgent;
import com.davidflex.supermarket.ontologies.company.concepts.Order;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Location;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handle the deliver of an order by the drone (flying to customer's location, deliver order and
 * comeback to warehouse).
 * Used by droneAgent.
 */
public class HandleDeliverBehaviour extends SimpleBehaviour {

    private static final Logger logger = LoggerFactory.getLogger(HandleDeliverBehaviour.class);

    private int step;
    private Location target;

    public HandleDeliverBehaviour(Agent a, Location target) {
        super(a);
        this.target = target;
        step = 0;
    }

    @Override
    public void action() {
        switch (step){
            case 0:
                // Fly to taget
                logger.info("Flying to target " + target);
                getAgent().addBehaviour(new FlyingBehaviour(getAgent(), this, target));
                step++;
                block();
                break;
            case 1:
                // Deliver order
                logger.info("Deliver order.");
                getAgent().addBehaviour(new DeliverOrderBehaviour(getAgent(), this));
                step++;
                break;
            case 2:
                // Wait until the order is delivered
                if(getOrder().isDelivered()) {
                    logger.info("Order is delivered!");
                    step++;
                } else {
                    block();
                }
                break;
            case 3:
                // Comeback to warehouse
                logger.info("Flying to warehouse " + getWarehouseLocation());
                getAgent().addBehaviour(new FlyingBehaviour(getAgent(), this, getWarehouseLocation()));
                step++;
                block();
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
