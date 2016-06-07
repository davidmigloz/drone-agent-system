package com.davidflex.supermarket.agents.behaviours;

import com.davidflex.supermarket.agents.customer.PersonalAgent;
import com.davidflex.supermarket.ontologies.ecommerce.elements.ContactRequest;
import com.davidflex.supermarket.ontologies.ecommerce.elements.ContactResponse;
import com.davidflex.supermarket.ontologies.ecommerce.elements.Location;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Inform the shop that the personalAgent wants to make an order.
 * Used by PersonalAgent.
 */
public class ContactBehaviour extends OneShotBehaviour {

    private static final Logger logger = LoggerFactory.getLogger(ContactBehaviour.class);

    private Location location;
    private String shopName;

    public ContactBehaviour(Agent a, Location location, String shopName) {
        super(a);
        this.location = location;
        this.shopName = shopName;
    }

    public void action() {
        try {
            // Prepare message
            logger.info("Contacting shop...");
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setSender(getAgent().getAID());
            AID shopAgent = new AID(shopName, AID.ISLOCALNAME);
            msg.addReceiver(shopAgent);
            msg.setLanguage(((PersonalAgent) getAgent()).getCodec().getName());
            msg.setOntology(((PersonalAgent) getAgent()).getOntology().getName());
            // Fill the content
            ContactRequest request = new ContactRequest(getAgent().getAID(), location);
            getAgent().getContentManager().fillContent(msg, request);
            // Send message
            getAgent().send(msg);
            // Get answer
            logger.info("Waiting answer from shop...");
            MessageTemplate mt = MessageTemplate.MatchSender(shopAgent);
            msg = getAgent().blockingReceive(mt);
            if (msg != null) {
                ContentElement ce = getAgent().getContentManager().extractContent(msg);
                if (ce instanceof ContactResponse) {
                    // Read content
                    ContactResponse cr = (ContactResponse) ce;
                    AID personalShopAgent = cr.getPersonalShopAgent();
                    // Start buying procces
                    
                } else {
                    logger.error("Wrong message received.");
                }
            } else {
                logger.error("Corrupted message received.");
            }
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Error filling msg.", e);
        }
    }
}
