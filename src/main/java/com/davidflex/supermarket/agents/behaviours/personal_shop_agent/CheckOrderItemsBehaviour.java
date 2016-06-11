package com.davidflex.supermarket.agents.behaviours.personal_shop_agent;

import com.davidflex.supermarket.agents.shop.PersonalShopAgent;
import com.davidflex.supermarket.agents.utils.WarehousesComparator;
import com.davidflex.supermarket.ontologies.company.elements.*;
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
 * Then it sends back a list with the available items together with their prices to the customer.
 * Used by PersonalShopAgent.
 */
public class CheckOrderItemsBehaviour extends OneShotBehaviour{
    private static final Logger logger = LoggerFactory.getLogger(CheckOrderItemsBehaviour.class);

    public CheckOrderItemsBehaviour(Agent a) {
        super(a);
    }


    // *************************************************************************
    // Core functions
    // *************************************************************************
    @Override
    public void action() {
        logger.info("Checking items availability...");
        AID buyerAID = ((PersonalShopAgent)getAgent()).getOrder().getBuyer();

        // Request list of warehouses and sort them by closet distance
        logger.info("Request list of warehouses available.");
        List<Warehouse> warehouses = null;
        try {
            warehouses = getWarehouses(); //Return empty in case of no warehouses.
            warehouses.sort(new WarehousesComparator(((PersonalShopAgent) getAgent()).getOrder().getLocation()));
            logger.info("List of warehouses received (And sorted):");
            for(Warehouse w : warehouses) {
                logger.debug("Warehouse: " + w.getLocation().getX() + "/" + w.getLocation().getY());
            }
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Unable to recover the list of warehouses.");
            return;
        }

        //Error if no warehouse available.
        if(warehouses.isEmpty()){
            logger.info("No warehouses available.. Error sending message.");
            this.sendPurchaseError(buyerAID, "No warehouse available.");
            return; //Quit now
        }
        logger.info("At least one warehouse is available.");

        //Send the list of items to the closest warehouse and receive warehouses load.
        logger.info("Send request to warehouses (Starting by closest");
        List<Item> requested = ((PersonalShopAgent)getAgent()).getOrder().getItems();
        List<List<Item>> wLoad = null;
        wLoad = this.requestToWarehouses(warehouses, requested);

        //TODO Update: item list data in PersonalShopAgent could be updated

        //Send available items list to customer
        logger.info("Sending available items to customer...");
        //Create list from load
        List<Item> availableItems = new ArrayList<>();
        for(List<Item> listItems : wLoad){
            for(Item item : listItems){
                availableItems.add(item);
            }
        }
        //Send list. If error, stop here. (Can be lost connection etc)
        try {
            this.sendListToCustomer(buyerAID, availableItems);
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("[CRITIQUE] Unable to send list of item to customer.");
            return;
        }

        // CheckOrderItemsBehavior end and start Handle purchase
        logger.info("CheckOrderItemsBehavior is done and leave.");
        //TODO Critique: add the load in parameter.
        getAgent().addBehaviour(new HandlePurchaseBehaviour(getAgent()));
    }

    /**
     * Request the items to the given warehouses.
     * Request ask to the first warehouse on the list, if items are remaining,
     * ask to the second till all items get a warehouse and return the load for
     * each warehouses (returned list order follow the warehouse order)
     *
     * Note the load size can be smaller than warehouseList since it is filled
     * since the last warehouse with items. (For example, if for item A,B,C and
     * warehouses W1, W2, W3, W4, W5.
     * If W1 got B and W3 got A,C. Returned list will be size 3:
     * 0 -> B
     * 1 -> empty
     * 2 -> A,C
     *
     * @param warehouseList List of warehouse where to check for products
     * @param items         List of items to request
     * @return The load (list of Items) for the warehouses
     */
    private List<List<Item>> requestToWarehouses(List<Warehouse> warehouseList, List<Item> items) {
        List<Item> remaining    = items;
        List<Item> received     = null;
        List<List<Item>> wLoad  = new ArrayList<>(); //Items for each warehouse

        //Browse each warehouse for item availability and price.
        for(Warehouse w: warehouseList){
            try {
                this.sendListToWarehouse(w.getWarehouseAgent(), remaining);
                received = this.blockReceiveListFromWarehouse(w.getWarehouseAgent());
                wLoad.add(received); //Add load for this warehouse (Even if empty)
                remaining = checkMissingItems(received, remaining);
                //Stop if all items received.
                if(remaining.isEmpty()){
                    break;
                }
            } catch (Codec.CodecException | OntologyException ex) {
                //If a trouble appear while connecting with this warehouse, just
                //skipp it. (Connection can be lost for this warehouse).
                wLoad.add(new ArrayList<>()); //Empty list for this warehouse.
                logger.warn("A request to warehouse {} failed and has been skipped.", w.toString());
            }
        }
        return wLoad;
    }


    // *************************************************************************
    // Send functions
    // *************************************************************************
    /**
     * Sends a list of requested items to a warehouse.
     *
     * @param warehouse             Warehouse where to send request
     * @param items                 List of items send
     * @throws Codec.CodecException if error while sending
     * @throws OntologyException    if error while sending
     */
    private void sendListToWarehouse(AID warehouse, List<Item> items)
            throws Codec.CodecException, OntologyException {
        //Create message
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setSender(getAgent().getAID());
        msg.addReceiver(warehouse);
        msg.setLanguage(((PersonalShopAgent) getAgent()).getCodec().getName());
        msg.setOntology(((PersonalShopAgent) getAgent()).getCompanyOntology().getName());
        //Fill msg content and send it like a boss
        CheckStockItemsRequest request = new CheckStockItemsRequest(items);
        getAgent().getContentManager().fillContent(msg, request);
        getAgent().send(msg);
    }

    /**
     * Sends list of available products to the customer.
     *
     * @param buyer Buyer where to send item list
     * @param items List of available items
     * @throws Codec.CodecException if error while sending
     * @throws OntologyException    if error while sending
     */
    private void sendListToCustomer(AID buyer, List<Item> items) throws Codec.CodecException, OntologyException {
        // Prepare message
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setSender(getAgent().getAID());
        msg.addReceiver(buyer);
        msg.setLanguage(((PersonalShopAgent) getAgent()).getCodec().getName());
        msg.setOntology(((PersonalShopAgent) getAgent()).getShopOntology().getName());
        // Fill the content and send the message
        PurchaseRespond respond = new PurchaseRespond(items);
        getAgent().getContentManager().fillContent(msg, respond);
        getAgent().send(msg);
    }

    /**
     * Wait for a message from the given warehouse.
     * Message received should be a valid response for sock item, otherwise,
     * a empty list will be returned.
     *
     * @param warehouse Warehouse to wait for.
     * @return
     * @throws Codec.CodecException
     * @throws OntologyException
     */
    private List<Item> blockReceiveListFromWarehouse(AID warehouse) throws Codec.CodecException, OntologyException {
        MessageTemplate mt  = MessageTemplate.MatchSender(warehouse);
        ACLMessage      msg = this.getAgent().blockingReceive(mt);
        ContentElement  ce  = getAgent().getContentManager().extractContent(msg);
        if( ce instanceof CheckStockItemsResponse){
            CheckStockItemsResponse response = (CheckStockItemsResponse)ce;
            return response.getItems(); //Can be empty if no items
        }
        return new ArrayList<>(); //Shouldn't happen.
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
     * Return a list with missing element. (Element present in expected but missing in received).
     * Received list will in the best case contains all the expected element.
     *
     * @param received  Received list to test
     * @param expected  Expected list
     * @return New list with missing elements
     * @throws NullPointerException if listReceived is null
     */
    private List<Item> checkMissingItems(List<Item> received, List<Item> expected){
        List<Item> list = new ArrayList<>();
        for(Item i:expected){
            if(!received.contains(i)){
                list.add(i.clone()); //Clone to remove link between the 2 list.
            }
        }
        return list;
    }

    /**
     * Get the list of available warehouses (Registered in shop).
     *
     * @return List of warehouses.
     */
    private List<Warehouse> getWarehouses() throws Codec.CodecException, OntologyException {
        List<Warehouse> list = new ArrayList<>();

        //SEND REQUEST - Request message to the DF
        //Create message
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setSender(getAgent().getAID());
        msg.addReceiver(((PersonalShopAgent) getAgent()).getShopAgent());
        msg.setLanguage(((PersonalShopAgent) getAgent()).getCodec().getName());
        msg.setOntology(((PersonalShopAgent) getAgent()).getCompanyOntology().getName());
        // Fill the content and send it
        GetListWarehousesRequest request = new GetListWarehousesRequest();
        getAgent().getContentManager().fillContent(msg, request);
        getAgent().send(msg);

        //RECEIVE RESPONSE - Wait for answer.
        //Wait for message using template.
        MessageTemplate mt = MessageTemplate.MatchSender(((PersonalShopAgent) getAgent()).getShopAgent());
        ACLMessage response = getAgent().blockingReceive(mt);
        //Recover message content are get list.
        ContentElement ce = getAgent().getContentManager().extractContent(msg);
        if (ce instanceof GetListWarehousesResponse) {
            GetListWarehousesResponse wResonse = (GetListWarehousesResponse) ce;
            list = wResonse.getWarehouses();
        } else {
            logger.error("Wrong message received.");
            return list;
        }
        return list;
    }
}
