package com.davidflex.supermarket.agents.behaviours.personal_shop_agent;

import com.davidflex.supermarket.agents.shop.PersonalShopAgent;
import com.davidflex.supermarket.agents.utils.WarehousesComparator;
import com.davidflex.supermarket.ontologies.company.elements.CheckStockItemsRequest;
import com.davidflex.supermarket.ontologies.company.elements.GetListWarehousesRequest;
import com.davidflex.supermarket.ontologies.company.elements.GetListWarehousesResponse;
import com.davidflex.supermarket.ontologies.company.elements.Warehouse;
import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;
import com.davidflex.supermarket.ontologies.ecommerce.elements.PurchaseError;
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

import java.util.ArrayList;
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
        logger.info("Checking items availability...");
        AID buyerAID = ((PersonalShopAgent) getAgent()).getOrder().getBuyer();

        /* TODO TO delete when new implementation is ok.
        for(Item i : ((PersonalShopAgent) getAgent()).getOrder().getItems()) {
            i.setPrice(1); // Just for testing!!!
        }
        ((PersonalShopAgent) getAgent()).getOrder().getItems().get(0).setPrice(50);
        ((PersonalShopAgent) getAgent()).getOrder().getItems().remove(1);

        //logger.error("Error filling msg.", e);
        */

        // Request list of warehouses and sort them by closet distance
        logger.info("Request list of warehouses available.");
        List<Warehouse> warehouses = getWarehouses();
        warehouses.sort(new WarehousesComparator(((PersonalShopAgent) getAgent()).getOrder().getLocation()));
        logger.info("List of warehouses received (And sorted):");
        for(Warehouse w : warehouses) {
            logger.debug("Warehouse: " + w.getLocation().getX() + "/" + w.getLocation().getY());
        }

        //Error if no warehouse available.
        if(warehouses.isEmpty()){
            this.sendPurchaseError(buyerAID, "No warehouse available.");
            return; //Quit now
        }

        //Send the list of items to the closest warehouse
        logger.info("Send request to closest warehouse");

        //Wait for answer from warehouse

        // Send list to customer
        logger.info("Sending available items to customer...");

        // Handle purchase
        getAgent().addBehaviour(new HandlePurchaseBehaviour(getAgent()));
    }


    // *************************************************************************
    // Send functions
    // *************************************************************************
    /**
     * Sends the list of requested items to a warehouse.
     *
     * @param warehouse             warehouse where to send request
     * @throws Codec.CodecException if error
     * @throws OntologyException    if error
     */
    private void sendListToWarehouse(AID warehouse) throws Codec.CodecException, OntologyException {
        //Create message
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setSender(getAgent().getAID());
        msg.addReceiver(warehouse);
        msg.setLanguage(((PersonalShopAgent) getAgent()).getCodec().getName());
        msg.setOntology(((PersonalShopAgent) getAgent()).getCompanyOntology().getName());
        //Fill msg content and send it like a boss
        CheckStockItemsRequest request;
        request = new CheckStockItemsRequest(((PersonalShopAgent) getAgent()).getOrder().getItems());
        getAgent().getContentManager().fillContent(msg, request);
        getAgent().send(msg);
    }

    private List<Item> blockReceiveListFromWarehouse(AID warehouse){
        //TODO
        return null;
    }

    /**
     * Sends list of available products to the customer.
     */
    private void sendListToCustomer(AID buyer) throws Codec.CodecException, OntologyException {
        // Prepare message
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setSender(getAgent().getAID());
        msg.addReceiver(buyer);
        msg.setLanguage(((PersonalShopAgent) getAgent()).getCodec().getName());
        msg.setOntology(((PersonalShopAgent) getAgent()).getShopOntology().getName());
        // Fill the content and send the message
        PurchaseRespond respond = new PurchaseRespond(((PersonalShopAgent) getAgent()).getOrder().getItems());
        getAgent().getContentManager().fillContent(msg, respond);
        getAgent().send(msg);
    }

    /**
     * Send a error message to a buyer.
     *
     * @param buyer     Where to send error message
     * @param message   Error message.
     */
    private void sendPurchaseError(AID buyer, String message){
        try {
            // Prepare message
            ACLMessage msg = new ACLMessage(ACLMessage.FAILURE);
            msg.setSender(getAgent().getAID());
            msg.addReceiver(buyer);
            msg.setLanguage(((PersonalShopAgent) getAgent()).getCodec().getName());
            msg.setOntology(((PersonalShopAgent) getAgent()).getShopOntology().getName());
            // Fill the content and send the message
            PurchaseError errMsg = new PurchaseError(message);
            getAgent().getContentManager().fillContent(msg, errMsg);
            getAgent().send(msg);
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("[Err] Unable to send purchaseError message.");
        }
    }


    // *************************************************************************
    // Asset functions
    // *************************************************************************

    /**
     * Request the items to the given warehouses.
     * Request ask to the first warehouse on the list, if items are remaining,
     * ask to the second till all items get a warehouse.
     *
     * @param warehouseList List of warehouse where to check for products.
     * @throws Codec.CodecException If unable to send / receive message
     * @throws OntologyException    If unable to send / receive message
     */
    private void requestToWarehouses(List<Warehouse> warehouseList) throws Codec.CodecException, OntologyException {
        List<Item> remaining    = ((PersonalShopAgent) getAgent()).getOrder().getItems();
        List<Item> received     = null;

        //Browse each warehouse for item availability and price.
        for(Warehouse w: warehouseList){
            this.sendListToWarehouse(w.getWarehouseAgent());
            received = this.blockReceiveListFromWarehouse(w.getWarehouseAgent());
            remaining = checkMissingItems(received, remaining);
            //Stop if all items received.
            if(remaining == null || remaining.isEmpty()){
                break;
            }
        }
    }

    /**
     * Return a list with missing element. (Element present in expected but missing in received).
     * Received list should in the best case contains all the expected element.
     *
     * @param received  Received list to test
     * @param expected  Expected list
     * @return New list with missing element
     * @throws NullPointerException if listReceived is null
     */
    private List<Item> checkMissingItems(List<Item> received, List<Item> expected){
        List<Item> list = new ArrayList<>();
        for(Item i:expected){
            if(!received.contains(i)){
                list.add(i);
            }
        }
        return list;
    }

    /**
     * Get the list of available warehouses (Registered in shop).
     *
     * @return List of warehouses.
     */
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
