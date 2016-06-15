package com.davidflex.supermarket.agents.behaviours.drone_agent;

import com.davidflex.supermarket.agents.shop.DroneAgent;
import com.davidflex.supermarket.ontologies.company.concepts.Order;
import com.davidflex.supermarket.ontologies.ecommerce.predicates.DeliverRequest;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Deliver the order to custemer onces the drone is in customer's location.
 * Used by droneAgent.
 */
class DeliverOrderBehaviour extends SimpleBehaviour {

    private static final Logger logger = LoggerFactory.getLogger(DeliverOrderBehaviour.class);

    private Behaviour manager;
    private int step;
    private MessageTemplate mt;


    DeliverOrderBehaviour(Agent a, Behaviour manager) {
        super(a);
        this.manager = manager;
        step = 0;
        mt = MessageTemplate.MatchSender(getOrder().getBuyer());
    }

    @Override
    public void action() {
        switch (step) {
            case 0:
                // Send DeliverRequest
                logger.info("Sending DeliverRequest to customer...");
                sendRequest(getOrder().getBuyer());
                step++;
                break;
            case 1:
                // Wait to DeliverResponse
                logger.info("Waiting for DeliverResponse...");
                ACLMessage msg = getAgent().receive(mt);
                if(msg != null) {
                    // Response received -> order completed -> comeback warehouse
                    logger.info("Deliver response rececived!");
                    getOrder().setDelivered(true);
                    manager.restart();
                    step++;
                } else {
                    block();
                }
                break;
        }
    }

    @Override
    public boolean done() {
        return step > 1;
    }

    /**
     * Sends DeliverRequest to customer.
     */
    private void sendRequest(AID customer) {
        try {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setSender(getAgent().getAID());
            msg.addReceiver(customer);
            msg.setLanguage(((DroneAgent) getAgent()).getCodec().getName());
            msg.setOntology(((DroneAgent) getAgent()).getShopOntologyName());
            msg.setConversationId(getOrder().getId() + "");
            // Fill the content
            DeliverRequest dr = new DeliverRequest(getOrder().getItems());
            getAgent().getContentManager().fillContent(msg, dr);
            // Send message
            getAgent().send(msg);
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Error at sending DeliverRequest.", e);
        }
    }

    /**
     * Get order to deliver.
     */
    private Order getOrder() {
        return ((DroneAgent) getAgent()).getOrder();
    }
}
