package com.davidflex.supermarket.agents.behaviours.personal_shop_agent;

import com.davidflex.supermarket.agents.shop.PersonalShopAgent;
import com.davidflex.supermarket.agents.utils.WarehousesComparator;
import com.davidflex.supermarket.ontologies.company.elements.GetListWarehousesRequest;
import com.davidflex.supermarket.ontologies.company.elements.GetListWarehousesResponse;
import com.davidflex.supermarket.ontologies.company.elements.Warehouse;
import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;
import com.davidflex.supermarket.ontologies.ecommerce.elements.PurchaseRespond;
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

import java.util.List;

/**
 * Checks availability and price of all the items of the order.
 * Then it sends a list with the available items together with their prices to the customer.
 * Used by PersonalShopAgent.
 */
public class CheckOrderItemsBehaviour extends OneShotBehaviour{

    private static final Logger logger = LoggerFactory.getLogger(CheckOrderItemsBehaviour.class);

    public CheckOrderItemsBehaviour(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        // TODO check availability and prices
        logger.info("Cheking items...");
        for(Item i : ((PersonalShopAgent) getAgent()).getOrder().getItems()) {
            i.setPrice(1); // Just for testing!!!
        }
        ((PersonalShopAgent) getAgent()).getOrder().getItems().get(0).setPrice(50);
        ((PersonalShopAgent) getAgent()).getOrder().getItems().remove(1);

        // Request list of warehouses and sort them
        List<Warehouse> warehouses = getWarehouses();
        warehouses.sort(new WarehousesComparator(((PersonalShopAgent) getAgent()).getOrder().getLocation()));
        for(Warehouse w : warehouses) {
            logger.debug("Warehouse: " + w.getX() + "/" + w.getY());
        }

        // Send list to customer
        logger.info("Sending available items to customer...");
        sendList(((PersonalShopAgent) getAgent()).getOrder().getBuyer());

        // Handle purchase
        getAgent().addBehaviour(new HandlePurchaseBehaviour(getAgent()));
    }

    /**
     * Sends list of available products to the customer.
     */
    private void sendList(AID buyer) {
        try {
            // Prepare message
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setSender(getAgent().getAID());
            msg.addReceiver(buyer);
            msg.setLanguage(((PersonalShopAgent) getAgent()).getCodec().getName());
            msg.setOntology(((PersonalShopAgent) getAgent()).getShopOntology().getName());
            // Fill the content
            PurchaseRespond respond = new PurchaseRespond(((PersonalShopAgent) getAgent()).getOrder().getItems());
            getAgent().getContentManager().fillContent(msg, respond);
            // Send message
            getAgent().send(msg);
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Error filling msg.", e);
        }
    }

    private List<Warehouse> getWarehouses() {
        List<Warehouse> list = null;
        // Send request
        try {
            // Prepare message
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setSender(getAgent().getAID());
            msg.addReceiver(((PersonalShopAgent) getAgent()).getShopAgent());
            msg.setLanguage(((PersonalShopAgent) getAgent()).getCodec().getName());
            msg.setOntology(((PersonalShopAgent) getAgent()).getCompanyOntology().getName());
            // Fill the content
            GetListWarehousesRequest request = new GetListWarehousesRequest();
            getAgent().getContentManager().fillContent(msg, request);
            // Send message
            getAgent().send(msg);
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Error filling msg.", e);
        }
        // Get response
        try {
            MessageTemplate mt = MessageTemplate.MatchSender(((PersonalShopAgent) getAgent()).getShopAgent());
            ACLMessage msg = getAgent().blockingReceive(mt);
            if (msg != null) {
                ContentElement ce = getAgent().getContentManager().extractContent(msg);
                if (ce instanceof GetListWarehousesResponse) {
                    // Read content
                    GetListWarehousesResponse glwr = (GetListWarehousesResponse) ce;
                    list = glwr.getWarehouses();

                } else {
                    logger.error("Wrong message received.");
                }
            } else {
                logger.error("Corrupted message received.");
            }
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Error extracting msg.", e);
        }
        return list;
    }
}
