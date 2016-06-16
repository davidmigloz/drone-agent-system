package com.davidflex.supermarket.agents.behaviours.personal_shop_agent;

import com.davidflex.supermarket.agents.shop.PersonalShopAgent;
import com.davidflex.supermarket.agents.utils.ListHelper;
import com.davidflex.supermarket.ontologies.company.predicates.ConfirmPurchaseRequest;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Item;
import com.davidflex.supermarket.ontologies.ecommerce.actions.Purchase;
import com.davidflex.supermarket.ontologies.ecommerce.predicates.PurchaseError;
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
 * Finalize a purchase for an order.
 * Gets the purchase order with the list of items to purchase and contact with
 * the proper warehouses to deliver the items.
 *
 * Used by PersonalShopAgent.
 *
 * @since   June 10, 2016
 * @author  Constantin MASSON
 */
class HandlePurchaseBehaviour extends OneShotBehaviour{
    private static final Logger logger = LoggerFactory.getLogger(HandlePurchaseBehaviour.class);
    private List<ConfirmPurchaseRequest> listWarehousesLoad; //Load set for each warehouse

    /**
     * Create and start the HandlePurchaseBehavior.
     *
     * @param a     Agent linked with this behavior
     * @param list  List of ConfirmPurchaseRequest linked with this purchase
     */
    HandlePurchaseBehaviour(Agent a, List<ConfirmPurchaseRequest> list) {
        super(a);
        this.listWarehousesLoad = list;
    }


    // *************************************************************************
    // Override function
    // *****************************
    // ********************************************
    @Override
    public void action() {
        logger.info("Start handlePurchaseBehavior in PersonalShopAgent.");
        AID buyerAID = ((PersonalShopAgent) getAgent()).getOrder().getBuyer();

        try {
            //Recover the list from customer and update the list of purchase
            logger.info("Wait customer's list with items he actually wants to buy");
            List<Item> listItems = this.blockReceivePurchaseResponse(buyerAID);
            logger.info("List from customer received. Update load warehouses");
            //this.updateWarehousesLoad(listItems); //TODO Tmp deleted
            this.listWarehousesLoad.get(0).setItems(listItems); //TMP: manage one warehouse
            logger.info("Warehouses load updated.");

            //Send to each warehouse its load
            logger.info("Send ConfirmPurchaseRequest to each warehouse.");
            for(ConfirmPurchaseRequest load : this.listWarehousesLoad){
                this.sendListToWarehouse(load.getWarehouse().getWarehouseAgent(), load);
            }

            //Send done confirmation to buyer
            //TODO Update: wait for confirmation from warehouse and sent to customer.
            logger.info("Send 'done' to customer.");
            this.sendDone(buyerAID); //TODO TO change with update
        }
        catch (Codec.CodecException | OntologyException ex) {
            logger.error("Unable to process the received list from customer...");
            this.sendPurchaseErrorToCustomer(
                    buyerAID,
                    "Selling has been cancelled because of internal issue..."
            );
        }
    }


    // *************************************************************************
    // Core functions
    // *************************************************************************

    /**
     * Update the load for each warehouse according to the given list of items.
     * The original purchase list is the load assigned for each warehouse in case
     * the customer buy every items he asked. however, some of them can be
     * cancelled (Price to high etc). The new warehouse load must be updated
     * to manage these changes.
     *
     * After this process, the given listItems will be altered. (Items quantity)
     *
     * @param listItems List of items user actually wants to buy
     */
    private void updateWarehousesLoad(List<Item> listItems){
        //TODO To update (No working atm: to finish)
        int index, iQ, wQ;
        List<Item> originalStock;

        //Browse each warehouse load.
        //Reset item load for each warehouse and update it with actual item bought
        for(ConfirmPurchaseRequest load : this.listWarehousesLoad){
            originalStock = load.getItems(); //Remember the original load for this warehouse.
            load.setItems(new ArrayList<>()); //Reset the load for this warehouse

            //Compare warehouse list of item with customer list.
            for(Item item : listItems){
                //Skipp if this item has already been assigned.
                if(item.getQuantity() == 0){ continue; }
                index = ListHelper.indexOfEltClass(load.getItems(), item);
                //Skipp if this item isn't managed by this warehouse
                if(index == -1){ continue; }

                iQ = item.getQuantity();
                wQ = load.getItems().get(index).getQuantity();

                //If warehouse quantity can be fully loaded for this item.
                if(iQ > wQ){
                    item.setQuantity(iQ - wQ); //Update remaining items to process
                    //The load for item in warehouse doesn't change
                }
                //Else, the load required by warehouse is in fact more than asked by customer
                else {
                    load.getItems().get(index).setQuantity(item.getQuantity());
                    item.setQuantity(0); //We are done with this item
                }
            }
        }
    }


    // *************************************************************************
    // Messaging function (Send / Receive)
    // *************************************************************************

    /**
     * Send a ConfirmPurchaseRequest to one warehouse.
     * The warehouse should be the one in charge of this purchase, otherwise,
     * it might refuse it.
     *
     * @param warehouse             Warehouse where to send the bird
     * @param purchaseRequest       Purchase this warehouse should process
     * @throws Codec.CodecException If unable to create the message
     * @throws OntologyException    If unable to create the message
     */
    private void sendListToWarehouse(AID warehouse, ConfirmPurchaseRequest purchaseRequest)
            throws Codec.CodecException, OntologyException {
        //Create message
        ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
        msg.setSender(getAgent().getAID());
        msg.addReceiver(warehouse);
        msg.setLanguage(((PersonalShopAgent) getAgent()).getCodec().getName());
        msg.setOntology(((PersonalShopAgent) getAgent()).getCompanyOntology().getName());
        //Fill msg content and send it like a boss
        getAgent().getContentManager().fillContent(msg, purchaseRequest);
        getAgent().send(msg);
    }

    /**
     * Send confirmation that the order has been recorded successfully.
     *
     * @param buyer                 AID where to send message.
     * @throws Codec.CodecException if error while creating message
     * @throws OntologyException    if error while creating message
     */
    private void sendDone(AID buyer) throws Codec.CodecException, OntologyException {
        // Prepare message
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setSender(getAgent().getAID());
        msg.addReceiver(buyer);
        msg.setLanguage(((PersonalShopAgent) getAgent()).getCodec().getName());
        msg.setOntology(((PersonalShopAgent) getAgent()).getShopOntologyName());
        // Fill the content and send message
        Done d = new Done(new Action(getAgent().getAID(), new Purchase()));
        getAgent().getContentManager().fillContent(msg, d);
        getAgent().send(msg);
    }

    /**
     * Wait for purchase message from customer.
     * Received message should be a Purchase, otherwise, empty list is returned.
     *
     * @param buyerAID              Customer to wait for
     * @return                      List of items customer wants to buy
     * @throws Codec.CodecException if unable to process the received message
     * @throws OntologyException    if unable to process the received message
     */
    private List<Item> blockReceivePurchaseResponse(AID buyerAID)
            throws Codec.CodecException, OntologyException {
        //TODO update: possibly add a timeout
        MessageTemplate mt  = MessageTemplate.MatchSender(buyerAID);
        ACLMessage      msg = this.getAgent().blockingReceive(mt);
        ContentElement  ce  = getAgent().getContentManager().extractContent(msg);
        if(ce instanceof Action){
            Action a = (Action) ce;
            if(a.getAction() instanceof Purchase) {
                Purchase response = (Purchase) a.getAction();
                return response.getItems(); //Can be empty if no items
            }
        }
        return new ArrayList<>(); //In case of wrong message
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
}
