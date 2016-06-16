package com.davidflex.supermarket.agents.behaviours.personal_shop_agent;

import com.davidflex.supermarket.agents.shop.PersonalShopAgent;
import com.davidflex.supermarket.agents.utils.ListHelper;
import com.davidflex.supermarket.agents.utils.WarehousesComparator;
import com.davidflex.supermarket.ontologies.company.concepts.*;
import com.davidflex.supermarket.ontologies.company.predicates.*;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Item;
import com.davidflex.supermarket.ontologies.ecommerce.predicates.PurchaseError;
import com.davidflex.supermarket.ontologies.ecommerce.predicates.PurchaseRespond;
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
 * Checks availability and price of all the items of an order.
 * Then it sends back a list with the available items together with their prices
 * to the customer.
 * The same order can be handled by several warehouses. The load for each warehouse
 * is created and passed to HandlePurchaseBehavior.
 *
 * Used by PersonalShopAgent.
 *
 * @since   June 10, 2016
 * @author  Constantin MASSON
 */
class CheckOrderItemsBehaviour extends OneShotBehaviour{
    // *************************************************************************
    // Basics
    // *************************************************************************
    private static final Logger logger = LoggerFactory.getLogger(CheckOrderItemsBehaviour.class);
    private final int TIMEOUT_RECEIVE = 20000; //In milliseconds

    CheckOrderItemsBehaviour(Agent a) {
        super(a);
    }


    // *************************************************************************
    // Override functions
    // *************************************************************************
    @Override
    public void action() {
        logger.info("Checking items availability in warehouses...");
        AID buyerAID = ((PersonalShopAgent)getAgent()).getOrder().getBuyer();

        // Request list of warehouses
        logger.info("Request list of warehouses available.");
        List<Warehouse> warehouses;
        try {
            warehouses = getWarehouses();
        } catch (Codec.CodecException | OntologyException ex) {
            logger.error("Unable to recover the list of warehouses.", ex);
            this.sendPurchaseErrorToCustomer(buyerAID, "No warehouse available.");
            return; //Quit now
        }

        //Error if no warehouse available.
        if(warehouses.isEmpty()){
            logger.error("No warehouses available... Error message sent.");
            this.sendPurchaseErrorToCustomer(buyerAID, "No warehouse available.");
            return; //Quit now
        }

        //Sort warehouses by closet distance
        warehouses.sort(new WarehousesComparator(((PersonalShopAgent) getAgent()).getOrder().getLocation()));
        logger.info("List of warehouses received (And sorted).");
        for(Warehouse w : warehouses) {
            logger.debug("Warehouse: " + w.getLocation().getX() + "/" + w.getLocation().getY());
        }

        //Request the warehouse for the availability of items (Starting by closest)
        logger.info("Send request to warehouses (Starting by closest)");
        List<Item> requested = ((PersonalShopAgent)getAgent()).getOrder().getItems();
        List<ConfirmPurchaseRequest> listConfirm; //The future response list
        try {
            listConfirm = this.createWarehousesLoad(warehouses, requested);
            logger.info("Availability from warehouses received successfully.");
        } catch (Codec.CodecException | OntologyException ex) {
            logger.error("Unable to send request to warehouse.", ex);
            this.sendPurchaseErrorToCustomer(
                    buyerAID,
                    "Internal error occurred and selling has been cancelled."
            );
            return;
        }

        //Create the list of available items to send to customer (Price + quantity)
        List<Item> cList = new ArrayList<>();
        //Browse each warehouse list of item managed and add in customer list.
        for(ConfirmPurchaseRequest c : listConfirm){
            for(Item i : c.getItems()){
                int index = ListHelper.indexOfEltClass(cList, i);
                //If item is not in the list, add it
                if(index == -1){
                    cList.add(i);
                }
                //If item already in cList and quantity should be updated
                else if(cList.get(index).getClass().getSimpleName().equals(i.getClass().getSimpleName())){
                    cList.get(index).setQuantity(cList.get(index).getQuantity()+i.getQuantity());
                }
                //Otherwise, skipp this item
            }
        }

        //TODO Update: item list data in PersonalShopAgent could be updated if needed.

        //Send available items list to customer
        logger.info("Sending available items to customer...");
        try {
            this.sendListToCustomer(buyerAID, cList);
        } catch (Codec.CodecException | OntologyException ex) {
            logger.error("Unable to send the list of available items to the customer. ", ex);
            return; //Important to quite now instead of starting next behavior.
        }

        // CheckOrderItemsBehavior end and start Handle purchase
        logger.info("CheckOrderItemsBehavior is done and leave.");
        getAgent().addBehaviour(new HandlePurchaseBehaviour(getAgent(), listConfirm));
    }


    // *************************************************************************
    // Core functions
    // *************************************************************************

    /**
     * Request the items to the given warehouses.
     * Request ask to the first warehouse on the list, if items are remaining,
     * ask to the second till all items get a warehouse and return the list
     * of ConfirmPurchaseRequest (On for each warehouse that will be in charge
     * of this order.)
     *
     * If no warehouse can handle any item, an empty list will be returned.
     *
     * Note: The same item can be requested for 2 warehouse.
     * For example, 20 items A requested and 2 warehouses have each 10 of them.
     *
     * @param warehouseList List of warehouse where to check for products
     * @param items         List of items to request
     * @return              The list of ConfirmPurchaseRequest
     */
    private List<ConfirmPurchaseRequest> createWarehousesLoad(
            List<Warehouse> warehouseList, List<Item> items)
            throws Codec.CodecException, OntologyException {
        List<ConfirmPurchaseRequest> listConfirm = new ArrayList<>();
        List<Item> remaining = items;
        List<Item> received; //Items current warehouse can handle.

        //Browse each warehouse for item availability and price.
        for(Warehouse w: warehouseList){
            this.sendListToWarehouse(w.getWarehouseAgent(), remaining);
            received = this.blockReceiveListFromWarehouse(
                    w.getWarehouseAgent(),
                    TIMEOUT_RECEIVE
            );

            //If this warehouse can't handle any items, switch to next.
            if(received == null || received.isEmpty()){ continue; }

            //Otherwise, create the ConfirmPurchaseRequest for this warehouse
            ConfirmPurchaseRequest confirm = new ConfirmPurchaseRequest(
                    ((PersonalShopAgent)getAgent()).getOrder(), received, w
            );
            listConfirm.add(confirm);

            //Check the remaining items, continue if still some.
            //TODO Update: temporary removed (Function not working)
            break; //Currently work with only one warehouse
            /*
            remaining = checkMissingItems(received, remaining);
            if(remaining == null || remaining.isEmpty()){
                break; //Every item has been assigned to a warehouse.
            }
            */
        }
        return listConfirm;
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
    private void sendListToCustomer(AID buyer, List<Item> items)
            throws Codec.CodecException, OntologyException {
        // Prepare message
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setSender(getAgent().getAID());
        msg.addReceiver(buyer);
        msg.setLanguage(((PersonalShopAgent) getAgent()).getCodec().getName());
        msg.setOntology(((PersonalShopAgent) getAgent()).getShopOntologyName());
        // Fill the content and send the message
        long orderID = ((PersonalShopAgent) getAgent()).getOrder().getId();
        PurchaseRespond respond = new PurchaseRespond(orderID, items);
        getAgent().getContentManager().fillContent(msg, respond);
        getAgent().send(msg);
    }

    /**
     * Send a error message to a buyer.
     * If unable to send, err message is displayed (Customer disconnected or unreachable)
     *
     * @param buyer     Where to send error message
     * @param message   Error message.
     */
    private void sendPurchaseErrorToCustomer(AID buyer, String message){
        try {
            // Prepare message
            ACLMessage msg = new ACLMessage(ACLMessage.FAILURE);
            msg.setSender(getAgent().getAID());
            msg.addReceiver(buyer);
            msg.setLanguage(((PersonalShopAgent) getAgent()).getCodec().getName());
            msg.setOntology(((PersonalShopAgent) getAgent()).getShopOntologyName());
            // Fill the content and send the message
            PurchaseError errMsg = new PurchaseError(message);
            getAgent().getContentManager().fillContent(msg, errMsg);
            getAgent().send(msg);
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Unable to send purchaseError message.");
        }
    }

    /**
     * Wait for a message from the given warehouse.
     * Message received should be a valid response for stock item, otherwise,
     * a empty list will be returned. (CheckStockItemsResponse expected)
     *
     * @param warehouse             Warehouse to wait for
     * @param timeout               Timeout before stopping blocking
     * @return                      List of items (With they quantity)
     * @throws Codec.CodecException if unable to recover received message
     * @throws OntologyException    if unable to recover received message
     */
    private List<Item> blockReceiveListFromWarehouse(AID warehouse, int timeout)
            throws Codec.CodecException, OntologyException {
        MessageTemplate mt  = MessageTemplate.MatchSender(warehouse);
        ACLMessage      msg = this.getAgent().blockingReceive(mt, timeout);
        ContentElement  ce  = getAgent().getContentManager().extractContent(msg);
        if(ce instanceof CheckStockItemsResponse){
            CheckStockItemsResponse response = (CheckStockItemsResponse)ce;
            return response.getItems(); //Can be empty if no items
        }
        return new ArrayList<>(0); //Shouldn't happen, but in case of.
    }


    // *************************************************************************
    // Asset functions
    // *************************************************************************
    /**
     * Return a list with missing element. (Element present in expected but missing in received).
     * Received list will in the best case contains all the expected element.
     * Important: the quantity for each item matter. (If quantity is expected for
     * an item A, and only 2 are find, still one is required).
     *
     * @param received  Received list to test
     * @param expected  Expected list
     * @return New list with missing actions
     * @throws NullPointerException if received is null
     */
    private List<Item> checkMissingItems(List<Item> received, List<Item> expected){
        //TODO Important: Integrity to test (Function is not fully tested)
        List<Item> list = new ArrayList<>();
        int remainStock, index;

        //Browse each item from expected list
        for(Item item : expected){
            //Item not in the received, add to list
            index = ListHelper.indexOfEltClass(received, item);
            if(index == -1){
                list.add(item);
            }
            //Else, check how much item remains in expected after sub received stock.
            remainStock = item.getQuantity() - received.get(index).getQuantity();
            //If some items remain for this type
            if(remainStock > 0){
                Item i = received.get(index).clone();
                i.setPrice(received.get(index).getPrice());
                i.setQuantity(remainStock);
                list.add(i);
            }
        }
        return list;
    }

    /**
     * Get the list of available warehouses (Registered in shop).
     *
     * @return List of warehouses. (Empty if no warehouses)
     */
    private List<Warehouse> getWarehouses() throws Codec.CodecException, OntologyException {
        List<Warehouse> list = null;

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

        //Recover message content and get list.
        ContentElement ce = getAgent().getContentManager().extractContent(response);
        if (ce instanceof GetListWarehousesResponse) {
            GetListWarehousesResponse wResonse = (GetListWarehousesResponse) ce;
            list = wResonse.getWarehouses();
        } else {
            logger.error(
                    "GetListWarehousesResponse expected but wrong type received. " +
                    "The list of warehouses will probably be wrong (Empty)."
            );
        }
        return list != null ? list : new ArrayList<>(0);
    }
}
