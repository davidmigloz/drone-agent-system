package com.davidflex.supermarket.agents.behaviours;

import com.davidflex.supermarket.agents.shop.PersonalShopAgent;
import com.davidflex.supermarket.ontologies.company.elements.AssignOrder;
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
 * Get order info send by shopAgent.
 * Used by personalShopAgent.
 */
public class GetCustomerInfoBehaviour extends OneShotBehaviour {

    private static final Logger logger = LoggerFactory.getLogger(GetCustomerInfoBehaviour.class);

    private MessageTemplate mt;

    public GetCustomerInfoBehaviour(Agent a) {
        super(a);
        mt = MessageTemplate.MatchOntology(((PersonalShopAgent) getAgent()).getCompanyOntology().getName());
    }

    @Override
    public void action() {
        ACLMessage msg = getAgent().blockingReceive(mt);
        if (msg != null) {
            try {
                ContentElement ce = getAgent().getContentManager().extractContent(msg);
                if (ce instanceof AssignOrder) {
                    logger.info("Order info received!");
                    // Get content
                    AssignOrder ao = (AssignOrder) ce;
                    // Save order
                    ((PersonalShopAgent) getAgent()).setOrder(ao.getOrder());
                    // Send confirmation
                    msg = msg.createReply();
                    getAgent().send(msg);
                    // Be prepared to receive order from customer
                    getAgent().addBehaviour(new GetOrderBehaviour(getAgent()));
                }
            } catch (Codec.CodecException | OntologyException e) {
                logger.error("Error at extracting message", e);
            }
        }
    }
}
