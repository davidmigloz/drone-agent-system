package com.davidflex.supermarket.agents.behaviours;

import com.davidflex.supermarket.agents.shop.PersonalShopAgent;
import com.davidflex.supermarket.ontologies.ecommerce.elements.PurchaseRequest;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Get the list of items that the user wants to buy.
 * Used by PersonalShopAgent.
 */
public class GetOrderBehaviour extends OneShotBehaviour {

    private static final Logger logger = LoggerFactory.getLogger(GetOrderBehaviour.class);

    private MessageTemplate mt;

    public GetOrderBehaviour(Agent a) {
        super(a);
        mt = MessageTemplate.MatchSender(((PersonalShopAgent) getAgent()).getOrder().getBuyer());
    }

    @Override
    public void action() {
        ACLMessage msg = getAgent().blockingReceive(mt);
        if (msg != null) {
            try {
                ContentElement ce = getAgent().getContentManager().extractContent(msg);
                if (ce instanceof PurchaseRequest) {
                    logger.info("Item list received!");
                    // Get content
                    PurchaseRequest pr = (PurchaseRequest) ce;
                    // Save list
                    ((PersonalShopAgent) getAgent()).getOrder().setItems(pr.getItem());
                    // Check items
                    getAgent().addBehaviour(new CheckOrderItemsBehaviour(getAgent()));
                }
            } catch (Codec.CodecException | OntologyException e) {
                logger.error("Error at extracting message", e);
            }
        }
    }
}
