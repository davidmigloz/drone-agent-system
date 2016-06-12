package com.davidflex.supermarket.agents.behaviours.personal_shop_agent;

import com.davidflex.supermarket.agents.shop.PersonalShopAgent;
import com.davidflex.supermarket.ontologies.company.elements.ConfirmPurchaseRequest;
import com.davidflex.supermarket.ontologies.ecommerce.elements.Purchase;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Gets the purchase order with the list of items to purchase and contact with the proper warehouses
 * to deliver the items.
 *  Used by PersonalShopAgent.
 */
class HandlePurchaseBehaviour extends OneShotBehaviour{

    private static final Logger logger = LoggerFactory.getLogger(HandlePurchaseBehaviour.class);
    private List<ConfirmPurchaseRequest> listPurchase;

    HandlePurchaseBehaviour(Agent a, List<ConfirmPurchaseRequest> list) {
        super(a);
        this.listPurchase = list;
    }

    @Override
    public void action() {
        // TODO contact with warehouse/s to deliver order

        // Send Done
        sendDone(((PersonalShopAgent) getAgent()).getOrder().getBuyer());

    }

    /**
     * Send confirmation that the order has been recorded succesfully.
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
            Done d = new Done(new Action(getAgent().getAID(), new Purchase()));
            getAgent().getContentManager().fillContent(msg, d);
            // Send message
            getAgent().send(msg);
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Error filling msg.", e);
        }
    }
}
