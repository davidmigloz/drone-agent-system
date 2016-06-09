package com.davidflex.supermarket.agents.behaviours;

import com.davidflex.supermarket.agents.shop.PersonalShopAgent;
import com.davidflex.supermarket.agents.shop.ShopAgent;
import com.davidflex.supermarket.ontologies.company.elements.AssignOrder;
import com.davidflex.supermarket.ontologies.company.elements.Order;
import com.davidflex.supermarket.ontologies.ecommerce.elements.ContactRequest;
import com.davidflex.supermarket.ontologies.ecommerce.elements.ContactResponse;
import com.davidflex.supermarket.ontologies.ecommerce.elements.Location;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.StaleProxyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listen the ContactRequest from PersonaAgents and manages them.
 * Used by ShopAgent.
 */
public class ListenNewOrdersBehaviour extends CyclicBehaviour {

    private static final Logger logger = LoggerFactory.getLogger(ListenNewOrdersBehaviour.class);

    private MessageTemplate mtOnto;

    public ListenNewOrdersBehaviour(Agent a) {
        super(a);
        mtOnto = MessageTemplate.MatchOntology(((ShopAgent) getAgent()).getShopOntology().getName());
    }

    @Override
    public void action() {
        ACLMessage msg = getAgent().receive(mtOnto);
        if (msg != null) {
            try {
                ContentElement ce = getAgent().getContentManager().extractContent(msg);
                if (ce instanceof ContactRequest) {
                    // Get content
                    ContactRequest cr = (ContactRequest) ce;

                    // Register order
                    logger.info("New order received!");
                    long orderID = ((ShopAgent) getAgent()).addNewOrder(cr.getLocation());

                    // Assign new PersonalShopAgent
                    logger.info("Assigning personalShopAgent...");
                    String personalShopAgentName = "personalShopAgent#" + orderID;
                    ((ShopAgent) getAgent()).getContainer().createNewAgent(personalShopAgentName,
                            PersonalShopAgent.class.getName(), new Object[]{}).start();

                    // Send order info to PersonalShopAgent
                    logger.info("Sending order info to personalShopAgent...");
                    AID personalShopAgent = new AID(personalShopAgentName, AID.ISLOCALNAME);
                    sendInfoToPSA(personalShopAgent, orderID, cr.getBuyer(), cr.getLocation());

                    // Get confirmation
                    MessageTemplate mtPSA = MessageTemplate.MatchSender(personalShopAgent);
                    msg = getAgent().blockingReceive(mtPSA);
                    if (msg != null) {
                        // Send info to customer
                        logger.info("Sending ContactResponse to customer...");
                        replyCustomer(cr.getBuyer(), personalShopAgent);
                    }
                } else {
                    logger.error("Wrong message received.");
                }
            } catch (Codec.CodecException | OntologyException e) {
                logger.error("Error at extracting message", e);
            } catch (StaleProxyException e) {
                logger.error("Error at creating new personalShopAgent", e);
            }
        } else {
            block();
        }
    }

    /**
     * Sends a message to the new personalShopAgent with the order info.
     */
    private void sendInfoToPSA(AID personalShopAgent, long orderID, AID customer, Location location) {
        try {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setSender(getAgent().getAID());
            msg.addReceiver(personalShopAgent);
            msg.setLanguage(((ShopAgent) getAgent()).getCodec().getName());
            msg.setOntology(((ShopAgent) getAgent()).getCompanyOntology().getName());
            // Fill the content
            Order order = new Order(orderID, customer, location);
            AssignOrder request = new AssignOrder(order);
            getAgent().getContentManager().fillContent(msg, request);
            // Send message
            getAgent().send(msg);
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Error at sending order info.", e);
        }
    }

    /**
     * Sends the ContactResponse to the customer.
     */
    private void replyCustomer(AID customer, AID personalShopAgent) {
        try {
            ACLMessage msg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
            msg.setSender(getAgent().getAID());
            msg.addReceiver(customer);
            msg.setLanguage(((ShopAgent) getAgent()).getCodec().getName());
            msg.setOntology(((ShopAgent) getAgent()).getShopOntology().getName());
            // Fill the content
            ContactResponse response = new ContactResponse(personalShopAgent);
            getAgent().getContentManager().fillContent(msg, response);
            // Send message
            getAgent().send(msg);
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Error at sending info to customer.", e);
        }
    }
}
