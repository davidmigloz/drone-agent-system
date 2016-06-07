package com.davidflex.supermarket.agents.behaviours;

import com.davidflex.supermarket.agents.customer.PersonalAgent;
import com.davidflex.supermarket.agents.utils.DFUtils;
import com.davidflex.supermarket.ontologies.ecommerce.ECommerceOntologyVocabulary;
import com.davidflex.supermarket.ontologies.ecommerce.elements.ContactRequest;
import com.davidflex.supermarket.ontologies.ecommerce.elements.ContactResponse;
import com.davidflex.supermarket.ontologies.ecommerce.elements.Location;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
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
            // Look for the appropiate shop
            AID shopAgent;
            DFAgentDescription[] result =
                    DFUtils.searchInDFbyType(ECommerceOntologyVocabulary.SHOP_TYPE, myAgent);
            if(result.length > 0) {
                shopAgent = result[0].getName();
            } else {
                logger.error("No shops of given type");
                ((PersonalAgent) myAgent).cancelOrder("No shops available.");
                return;
            }
            // Prepare message
            logger.info("Contacting shop...");
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setSender(getAgent().getAID());
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
                    logger.info("Got response from shop.");
                    // Read content
                    ContactResponse cr = (ContactResponse) ce;
                    AID personalShopAgent = cr.getPersonalShopAgent();
                    logger.info("My PSA is: " + personalShopAgent.getName());
                    // Start buying procces
                    
                } else {
                    logger.error("Wrong message received.");
                }
            } else {
                logger.error("Corrupted message received.");
            }
        } catch (FIPAException e) {
            logger.error("Error with DF", e);
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Error filling msg.", e);
        }
    }
}
