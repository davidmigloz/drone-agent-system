package com.davidflex.supermarket.agents.behaviours.drone_agent;

import com.davidflex.supermarket.agents.shop.DroneAgent;
import com.davidflex.supermarket.ontologies.company.actions.AssignOrder;
import com.davidflex.supermarket.ontologies.company.concepts.Order;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Location;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
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

    public HandleDeliverBehaviour(Agent a) {
        super(a);
        step = 0;
    }

    @Override
    public void action() {
        switch (step){
            case 0:
                //Assign order
                logger.info("Assign order to the drone");
                try {
                    this.blockReceiveReceiveOrderMessage();
                } catch (Codec.CodecException | OntologyException ex) {
                    logger.error("Unable to recover the received message");
                    //TODO Important To handle
                    return;
                }
                step++;
                break;
            case 1:
                // Fly to taget
                logger.info("Flying to target " + getOrder().getLocation());
                getAgent().addBehaviour(new FlyingBehaviour(getAgent(), this,
                        getOrder().getLocation()));
                step++;
                block();
                break;
            case 2:
                // Deliver order
                logger.info("Deliver order.");
                getAgent().addBehaviour(new DeliverOrderBehaviour(getAgent(), this));
                step++;
                break;
            case 3:
                // Wait until the order is delivered
                if(getOrder().isDelivered()) {
                    logger.info("Order is delivered!");
                    step++;
                } else {
                    block();
                }
                break;
            case 4:
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
        if(step > 4) {
            // Drone in warehouse -> Unregister from shopAgent
            getAgent().addBehaviour(new UnregisterBehaviour());
            step = 0;
        }
        return false;
    }

    /**
     * Block till an order is assigned to this drone.
     *
     * @throws Codec.CodecException If unable to process the message
     * @throws OntologyException    If unable to process the message
     */
    private void blockReceiveReceiveOrderMessage() throws Codec.CodecException, OntologyException {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchSender(((DroneAgent)getAgent()).getWarehouse().getWarehouseAgent()),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
        );
        //Wait for the message
        ACLMessage msg = getAgent().blockingReceive(mt);
        ContentElement ce = getAgent().getContentManager().extractContent(msg);
        if(!(ce instanceof Action)){
            logger.error("Drone receive wrong ");
            return;
            //TODO Important To handle
        }

        //Recover the message content
        Action a = (Action)ce;
        if(!(a.getAction() instanceof AssignOrder)){
            logger.error("Drone receive wrong ");
            return;
            //TODO Important To handle
        }

        //Recover the order and set it in the drone
        AssignOrder order = (AssignOrder)a.getAction();
        ((DroneAgent)getAgent()).setOrder(order.getOrder());
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
