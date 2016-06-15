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
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handle the deliver of an order by the drone
 * (Flying to customer's location, deliver order and comeback to warehouse).
 *
 * Used by droneAgent.
 */
public class HandleDeliverBehaviour extends CyclicBehaviour {

    private static final Logger logger = LoggerFactory.getLogger(HandleDeliverBehaviour.class);

    private int step;
    private Behaviour informPositionBehaviour;

    public HandleDeliverBehaviour(Agent a) {
        super(a);
        step = 0;
        // Inform position periodically
        informPositionBehaviour = new InformPositionBehaviour(getAgent());
    }

    @Override
    public void action() {
        switch (step) {
            case 0:
                //Assign order (Drone in the warehouse)
                logger.info("Wait for an order...");
                try {
                    this.blockReceiveReceiveOrderMessage();
                    logger.info("Assign order to the drone");
                } catch (Codec.CodecException | OntologyException ex) {
                    logger.error("Unable to recover the received message");
                    //TODO Important To handle
                    return;
                }
                step++;
                break;
            case 1:
                // Fly to target
                logger.info("Flying to target " + getOrder().getLocation());
                getAgent().addBehaviour(informPositionBehaviour);
                getAgent().addBehaviour(
                        new FlyingBehaviour(getAgent(), this, getOrder().getLocation())
                );
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
                if (getOrder().isDelivered()) {
                    logger.info("Order is delivered!");
                    step++;
                } else {
                    block();
                }
                break;
            case 4:
                // Comeback to warehouse
                logger.info("Flying to warehouse " + getWarehouseLocation());
                getAgent().addBehaviour(
                        new FlyingBehaviour(getAgent(), this, getWarehouseLocation())
                );
                step++;
                block();
                break;
            case 5:
                //Deregister the drone
                getAgent().addBehaviour(new UnregisterBehaviour());
                step++;
                break;
            case 6:
                //Remove the inform position behavior and reset drone state
                getAgent().removeBehaviour(informPositionBehaviour);
                step = 0;
                break;
        }
    }

    /**
     * Block till an order is assigned to this drone.
     *
     * @throws Codec.CodecException If unable to process the message
     * @throws OntologyException    If unable to process the message
     */
    private void blockReceiveReceiveOrderMessage() throws Codec.CodecException, OntologyException {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchSender(((DroneAgent) getAgent()).getWarehouse().getWarehouseAgent()),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
        );
        //Wait for the message
        ACLMessage msg = getAgent().blockingReceive(mt);
        ContentElement ce = getAgent().getContentManager().extractContent(msg);
        if (!(ce instanceof Action)) {
            logger.error("Drone receive wrong ");
            return;
            //TODO Important To handle
        }

        //Recover the message content
        Action a = (Action) ce;
        if (!(a.getAction() instanceof AssignOrder)) {
            logger.error("Drone receive wrong ");
            return;
            //TODO Important To handle
        }

        //Recover the order and set it in the drone
        AssignOrder order = (AssignOrder) a.getAction();
        ((DroneAgent) getAgent()).setOrder(order.getOrder());
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
