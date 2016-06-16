package com.davidflex.supermarket.agents.behaviours.personal_agent;

import com.davidflex.supermarket.agents.customer.PersonalAgent;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Item;
import com.davidflex.supermarket.ontologies.ecommerce.actions.*;
import com.davidflex.supermarket.ontologies.ecommerce.predicates.PurchaseError;
import com.davidflex.supermarket.ontologies.ecommerce.predicates.PurchaseRequest;
import com.davidflex.supermarket.ontologies.ecommerce.predicates.PurchaseRespond;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Tells PersonalShopAgent the items to buy to check disponibility and price and finally orders the available
 * items that fullfil the user requirements (max. price).
 * Used by PersonalAgent.
 */
class NegociationBehaviour extends OneShotBehaviour {

    private static final Logger logger = LoggerFactory.getLogger(NegociationBehaviour.class);

    private AID personalShopAgent;
    private List<Item> finalItemList;
    private MessageTemplate mt;

    NegociationBehaviour(Agent a, AID personalShopAgent) {
        super(a);
        this.personalShopAgent = personalShopAgent;
        mt = MessageTemplate.MatchSender(personalShopAgent);
    }

    @Override
    public void action() {
        // Send order to personalShopAgent
        logger.info("Sending list of items to " + personalShopAgent.getLocalName());
        ((PersonalAgent) getAgent()).printStatus(
                "Sending list of items to " + personalShopAgent.getLocalName() + "...");
        sendItems(personalShopAgent);

        // Get reply and check availability and prices
        try {
            logger.info("Waiting for reply...");
            ACLMessage msg = getAgent().blockingReceive(mt);
            ContentElement ce = getAgent().getContentManager().extractContent(msg);
            if (ce instanceof PurchaseRespond) {
                logger.info("Checking items...");
                ((PersonalAgent) getAgent()).printStatus("Reply received. Checking items...");
                // Read content
                PurchaseRespond pr = (PurchaseRespond) ce;
                // Check items
                ((PersonalAgent) getAgent()).setOrderId(pr.getOrderID());
                finalItemList = checkItems(pr.getItem());
            } else if (ce instanceof PurchaseError) {
                logger.error("Error occurred (PurchaseError received)");
                // Read content
                PurchaseError pe = (PurchaseError) ce;
                // Cancel order
                ((PersonalAgent) getAgent()).cancelOrder(pe.getMessage());
                return;
            } else {
                logger.error("Wrong message received.");
                return;
            }
        } catch (OntologyException | Codec.CodecException e) {
            logger.error("Error extracting msg.", e);
            return;
        }

        // Check final list
        if(finalItemList.size() == 0) {
            logger.info("No items to buy");
            ((PersonalAgent) getAgent()).cancelOrder("No items to buy.");
            // TODO send cancel message to personalShopAgent
            return;
        }

        // Send purchase message with the final order
        logger.info("Sending order to " + personalShopAgent.getLocalName());
        ((PersonalAgent) getAgent()).printStatus("Sending order to " + personalShopAgent.getLocalName() + "...");
        sendOrder(personalShopAgent);

        // Get reply
        try {
            logger.info("Waiting answer from " + personalShopAgent.getLocalName());
            ACLMessage msg = getAgent().blockingReceive(mt);
            if (msg != null) {
                ContentElement ce = getAgent().getContentManager().extractContent(msg);
                if (ce instanceof Done) {
                    // Order is done
                    logger.info("Order done.");
                    ((PersonalAgent) getAgent()).printStatus("Order registered!");
                    ((PersonalAgent) getAgent()).printStatus("Order is being delivered...");
                    // Prepare for delivering
                    getAgent().addBehaviour(new HandleDeliverBehaviour(getAgent()));
                } else {
                    logger.error("Wrong message received.");
                }
            }
        } catch (OntologyException | Codec.CodecException e) {
            logger.error("Error extracting msg.", e);
        }
    }

    /**
     * Sends a ContactRequest with the list of items to the shop.
     */
    private void sendItems(AID personalShopAgent) {
        try {
            // Prepare message
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setSender(getAgent().getAID());
            msg.addReceiver(personalShopAgent);
            msg.setLanguage(((PersonalAgent) getAgent()).getCodec().getName());
            msg.setOntology(((PersonalAgent) getAgent()).getOntology().getName());
            // Fill the content
            PurchaseRequest request = new PurchaseRequest(((PersonalAgent) getAgent()).getListItems());
            getAgent().getContentManager().fillContent(msg, request);
            // Send message
            getAgent().send(msg);
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Error filling msg.", e);
        }
    }

    private void sendOrder(AID personalShopAgent) {
        try {
            // Prepare message
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setSender(getAgent().getAID());
            msg.addReceiver(personalShopAgent);
            msg.setLanguage(((PersonalAgent) getAgent()).getCodec().getName());
            msg.setOntology(((PersonalAgent) getAgent()).getOntology().getName());
            // Fill the content
            Purchase request = new Purchase(finalItemList);
            Action a = new Action(getAgent().getAID(), request);
            getAgent().getContentManager().fillContent(msg, a);
            // Send message
            getAgent().send(msg);
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Error filling msg.", e);
        }
    }

    /**
     * Checks the received list of items with the user list, checking availability and max. prices.
     *
     * @return list of items that fullfil requirements
     */
    private List<Item> checkItems(List<Item> receivedList) {
        List<Item> result = new ArrayList<>();
        boolean checked;

        // For each item in user list
        for (Item itUser : ((PersonalAgent) getAgent()).getObservableListItems()) {
            checked = false;
            // Check if it is in the receive list
            for (int j = 0; j < receivedList.size(); j++) {
                Item itShop = receivedList.get(j);
                if (itUser.getClass().getSimpleName().equals(itShop.getClass().getSimpleName())) {
                    receivedList.remove(j);
                    checked = true;
                    // Check price
                    itUser.setPrice(itShop.getPrice());
                    if (itShop.getPrice() > itUser.getMaxPrice()) {
                        // If more expensive -> don't buy
                        itUser.setStatus("Max. price violation.");
                        ((PersonalAgent) getAgent()).printStatus(
                                "The price of " + itUser.getClass().getSimpleName() + " violates max. price restriction ("
                                        + itShop.getPrice() + ">" + itUser.getMaxPrice() + ").");
                    } else {
                        // If price ok -> buy it
                        result.add(itShop);
                        itUser.setStatus("In progress...");
                    }
                }
            }
            // Check if not available
            if (!checked) {
                itUser.setStatus("Not available.");
                ((PersonalAgent) getAgent()).printStatus(
                        "The product " + itUser.getClass().getSimpleName() + " is not available.");
            }
        }
        return result;
    }
}
