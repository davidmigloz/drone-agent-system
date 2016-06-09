package com.davidflex.supermarket.agents.behaviours.personal_shop_agent;

import com.davidflex.supermarket.agents.shop.PersonalShopAgent;
import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;
import com.davidflex.supermarket.ontologies.ecommerce.elements.PurchaseRespond;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checks availability and price of all the items of the order.
 * Then it sends a list with the available items together with their prices to the customer.
 * Used by PersonalShopAgent.
 */
public class CheckOrderItemsBehaviour extends OneShotBehaviour{

    private static final Logger logger = LoggerFactory.getLogger(CheckOrderItemsBehaviour.class);

    public CheckOrderItemsBehaviour(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        // TODO check availability and prices
        logger.info("Cheking items...");
        for(Item i : ((PersonalShopAgent) getAgent()).getOrder().getItems()) {
            i.setPrice(1); // Just for testing!!!
        }
        ((PersonalShopAgent) getAgent()).getOrder().getItems().get(0).setPrice(50);
        ((PersonalShopAgent) getAgent()).getOrder().getItems().remove(1);

        // Send list to customer
        logger.info("Sending available items to customer...");
        sendList(((PersonalShopAgent) getAgent()).getOrder().getBuyer());
        // Handle purchase
        getAgent().addBehaviour(new HandlePurchaseBehaviour(getAgent()));
    }

    /**
     * Sends list of available products to the customer.
     */
    private void sendList(AID buyer) {
        try {
            // Prepare message
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setSender(getAgent().getAID());
            msg.addReceiver(buyer);
            msg.setLanguage(((PersonalShopAgent) getAgent()).getCodec().getName());
            msg.setOntology(((PersonalShopAgent) getAgent()).getShopOntology().getName());
            // Fill the content
            PurchaseRespond respond = new PurchaseRespond(((PersonalShopAgent) getAgent()).getOrder().getItems());
            getAgent().getContentManager().fillContent(msg, respond);
            // Send message
            getAgent().send(msg);
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Error filling msg.", e);
        }
    }
}
