package com.davidflex.supermarket.agents.behaviours.personal_agent;

import com.davidflex.supermarket.agents.customer.PersonalAgent;
import com.davidflex.supermarket.agents.utils.DFUtils;
import com.davidflex.supermarket.ontologies.ecommerce.ECommerceOntologyVocabulary;
import com.davidflex.supermarket.ontologies.ecommerce.predicates.ContactRequest;
import com.davidflex.supermarket.ontologies.ecommerce.predicates.ContactResponse;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Location;
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

    public ContactBehaviour(Agent a, Location location) {
        super(a);
        this.location = location;
    }

    public void action() {
        try {
            // Look for the appropiate shop
            AID shopAgent;
            DFAgentDescription[] result =
                    DFUtils.searchInDFbyType(ECommerceOntologyVocabulary.SHOP_TYPE, getAgent());
            if (result.length > 0) {
                shopAgent = result[0].getName();
            } else {
                logger.error("No shops of given type");
                ((PersonalAgent) getAgent()).cancelOrder("No shops available.");
                return;
            }

            // Contact shop
            logger.info("Contacting shop...");
            ((PersonalAgent) getAgent()).printStatus("Contacting " + shopAgent.getLocalName() + "...");
            sendMessageShop(shopAgent);

            // Wait reply
            logger.info("Waiting answer from shop...");
            MessageTemplate mt = MessageTemplate.MatchSender(shopAgent);
            ACLMessage msg = getAgent().blockingReceive(mt);
            ContentElement ce = getAgent().getContentManager().extractContent(msg);
            if (ce instanceof ContactResponse) {
                // Read content
                ContactResponse cr = (ContactResponse) ce;
                AID personalShopAgent = cr.getPersonalShopAgent();
                logger.info("Got response from shop. PSA: " + personalShopAgent.getLocalName());
                ((PersonalAgent) getAgent()).printStatus("Request accepted!");
                ((PersonalAgent) getAgent()).printStatus(personalShopAgent.getLocalName() + " assigned to the order.");
                // Start buying procces
                getAgent().addBehaviour(new NegociationBehaviour(getAgent(), personalShopAgent));
            } else {
                logger.error("Wrong message received.");
            }
        } catch (FIPAException e) {
            logger.error("Error with DF", e);
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Error extracting msg.", e);
        }
    }

    /**
     * Sends a ContactRequest to the shop.
     */
    private void sendMessageShop(AID shopAgent) {
        try {
            // Prepare message
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
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Error filling msg.", e);
        }
    }
}
