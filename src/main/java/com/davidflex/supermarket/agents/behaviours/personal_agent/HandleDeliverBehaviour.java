package com.davidflex.supermarket.agents.behaviours.personal_agent;

import com.davidflex.supermarket.agents.customer.PersonalAgent;
import com.davidflex.supermarket.ontologies.ecommerce.elements.DeliverRequest;
import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handle the deliver of the order done by one or more drones.
 * Used by PersonalAgent.
 */
class HandleDeliverBehaviour extends SimpleBehaviour {

    private static final Logger logger = LoggerFactory.getLogger(HandleDeliverBehaviour.class);

    private Map<String, Integer> orderItems;
    private MessageTemplate mt;

    HandleDeliverBehaviour(Agent a) {
        super(a);
        // Generate map with order details (Product name -> quantity)
        orderItems = new HashMap<>(((PersonalAgent) getAgent()).getObservableListItems().size());
        for(Item i :((PersonalAgent) getAgent()).getObservableListItems()) {
            orderItems.put(i.getClass().getSimpleName(), i.getQuantity());
        }
        // Listen to messages with conversationId = orderId
        mt = MessageTemplate.MatchConversationId(((PersonalAgent) getAgent()).getOrderId() + "");
        logger.info("Waiting deliver...");
    }

    @Override
    public void action() {
        try {
            ACLMessage msg = getAgent().blockingReceive(mt);
            ContentElement ce = getAgent().getContentManager().extractContent(msg);
            if (ce instanceof DeliverRequest) {
                DeliverRequest dr = (DeliverRequest) ce;
                updateStatus(dr.getItems());
            }
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Error extracting msg.", e);
        }
    }

    @Override
    public boolean done() {
        if(orderItems.isEmpty()) {
            ((PersonalAgent) getAgent()).printStatus("Order completed!!!");
            return true;
        }
        return false;
    }

    /**
     * Update the register of items to receive.
     * @param items received items
     */
    private void updateStatus(List<Item> items) {
        for(Item i : items) {
            String name = i.getClass().getSimpleName();
            int receivedQuantity = i.getQuantity();
            Integer quantity = orderItems.get(name);
            if(quantity != null) {
                ((PersonalAgent) getAgent()).printStatus("Received " + quantity + " " + name + ".");
                // Update quantity to receive
                Integer quantityToReceive = quantity - receivedQuantity;
                if(quantityToReceive > 0) {
                    ((PersonalAgent) getAgent()).updateItemStatus(name, quantity + " received...");
                    orderItems.put(name, quantityToReceive);
                } else {
                    ((PersonalAgent) getAgent()).updateItemStatus(name, "Completed!");
                    orderItems.remove(name);
                }
            } else {
                logger.info("Wrong product received: " + name);
            }
        }
    }
}
