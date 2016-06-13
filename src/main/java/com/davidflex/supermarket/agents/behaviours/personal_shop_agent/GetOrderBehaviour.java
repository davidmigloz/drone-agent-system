package com.davidflex.supermarket.agents.behaviours.personal_shop_agent;

import com.davidflex.supermarket.agents.shop.PersonalShopAgent;
import com.davidflex.supermarket.ontologies.company.actions.AssignOrder;
import com.davidflex.supermarket.ontologies.ecommerce.predicates.PurchaseRequest;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Get order info send by shopAgent and the items list send by the customer.
 * Used by personalShopAgent.
 */
public class GetOrderBehaviour extends OneShotBehaviour {

    private static final Logger logger = LoggerFactory.getLogger(GetOrderBehaviour.class);

    public GetOrderBehaviour(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        MessageTemplate mt;
        try {
            // Get order info from ShopAgent (orderID, buyer, location)
            mt = MessageTemplate.MatchOntology(((PersonalShopAgent) getAgent()).getCompanyOntology().getName());
            ACLMessage msg = getAgent().blockingReceive(mt);
            ContentElement ce = getAgent().getContentManager().extractContent(msg);
            if (ce instanceof Action) {
                logger.info("Order info received!");
                // Get content
                AssignOrder ao = (AssignOrder) ((Action) ce).getAction();
                // Save order and shopAgent
                ((PersonalShopAgent) getAgent()).setShopAgent(msg.getSender());
                ((PersonalShopAgent) getAgent()).setOrder(ao.getOrder());
                // Send confirmation
                msg = msg.createReply();
                getAgent().send(msg);
            }

            // Get item list from customer
            mt = MessageTemplate.MatchSender(((PersonalShopAgent) getAgent()).getOrder().getBuyer());
            msg = getAgent().blockingReceive(mt);
            ce = getAgent().getContentManager().extractContent(msg);
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
