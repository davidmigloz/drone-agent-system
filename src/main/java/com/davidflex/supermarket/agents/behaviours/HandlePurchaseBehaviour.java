package com.davidflex.supermarket.agents.behaviours;

import com.davidflex.supermarket.agents.shop.PersonalShopAgent;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gets the purchase order with the list of items to purchase and contact with the proper warehouses
 * to deliver the items.
 */
public class HandlePurchaseBehaviour extends OneShotBehaviour{

    private static final Logger logger = LoggerFactory.getLogger(HandlePurchaseBehaviour.class);

    public HandlePurchaseBehaviour(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        // TODO contact with warehouse/s to deliver order

        // Send Done
        sendDone(((PersonalShopAgent) getAgent()).getOrder().getBuyer());

    }

    /**
     * Sends list of available products to the customer.
     */
    private void sendDone(AID buyer) {
        try {
            // Prepare message
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setSender(getAgent().getAID());
            msg.addReceiver(buyer);
            msg.setLanguage(((PersonalShopAgent) getAgent()).getCodec().getName());
            msg.setOntology(((PersonalShopAgent) getAgent()).getShopOntology().getName());
            // Fill the content
            Done d = new Done();
            getAgent().getContentManager().fillContent(msg, d);
            // Send message
            getAgent().send(msg);
            // Handle purchase
            getAgent().addBehaviour(new HandlePurchaseBehaviour(getAgent()));
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Error filling msg.", e);
        }
    }
}
