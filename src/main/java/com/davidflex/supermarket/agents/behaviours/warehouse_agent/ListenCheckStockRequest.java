package com.davidflex.supermarket.agents.behaviours.warehouse_agent;

import com.davidflex.supermarket.agents.shop.WarehouseAgent;
import com.davidflex.supermarket.ontologies.company.predicates.CheckStockItemsRequest;
import com.davidflex.supermarket.ontologies.company.predicates.CheckStockItemsResponse;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Item;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Listen for incoming CheckStockItemRequest request and handle it.
 *
 * @since   June 14, 2016
 * @author  Constantin MASSON
 */
public class ListenCheckStockRequest extends CyclicBehaviour{

    private static final Logger logger = LoggerFactory.getLogger(ListenCheckStockRequest.class);
    private AID persoShopAID = null;

    public ListenCheckStockRequest(Agent agent) {
        super(agent);
    }


    // *************************************************************************
    // Override / Core functions
    // *************************************************************************
    @Override
    public void action() {
        logger.info("Start ListenCheckStockRequest behavior");

        //Check whether a message has been received and recover list of items
        List<Item> requestedItems   = null;
        List<Item> wItems           = null;
        try {
            //receive list from personal shop agent
            requestedItems = this.receiveCheckStockRequest();
            //If null, means no msg received and behavior must block and quit
            if(requestedItems == null){
                block();
                return;
            }

            //Here, means list has actually been received. process it
            logger.info("List received from personal shop agent. Start processing it.");
            wItems = this.processReceivedList(requestedItems);
            logger.info("List has been updated according to available stocks.");

            //Send back the response
            this.sendListResponse(wItems);
            logger.info("Response list has been sent back.");
        } catch (Exception ex) {
            logger.error("Unable to process the message", ex);
        }
    }


    // *************************************************************************
    // Send / Receive functions
    // *************************************************************************

    /**
     * Check whether a REQUEST message has been received and handle it.
     * If no message received or other type, block the behavior.
     * Message should be CheckShotItemRequest ontology, otherwise, exception.
     *
     * Fill the sender value with the sender of received message. (If received)
     *
     * @return List of item requested by customer or null if no message received
     * @throws  Codec.CodecException    If unable to recover the message
     * @throws  OntologyException       If unable to recover the message
     * @throws  Exception               If invalid message received
     */
    private List<Item> receiveCheckStockRequest() throws Exception {
        //Message should be a request with valid ontology
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        ACLMessage msg = this.getAgent().receive(mt);
        if(msg == null){
            return null;
        }

        //Process content if message received
        ContentElement ce = this.getAgent().getContentManager().extractContent(msg);
        //Message should be CheckStockItemsRequest
        if(!(ce instanceof CheckStockItemsRequest)){
            logger.error("Request message received but is not CheckStockItemsRequest");
            throw new Exception(
                    "Message received is a Request but should be CheckStockItemsRequest"
            );
        }
        this.persoShopAID = msg.getSender();
        return ((CheckStockItemsRequest) ce).getItems();
    }

    /**
     * Send the response list to the personalShopAgent that asked the list.
     *
     * @param responseList          Response list
     * @throws Codec.CodecException If unable to create the message
     * @throws OntologyException    If unable to create the message
     */
    private void sendListResponse(List<Item> responseList)
            throws Codec.CodecException, OntologyException {
        //Create message
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setSender(getAgent().getAID());
        msg.addReceiver(this.persoShopAID);
        msg.setLanguage(((WarehouseAgent) getAgent()).getCodec().getName());
        msg.setOntology(((WarehouseAgent) getAgent()).getOntology().getName());
        //Fill msg content and send it like a boss
        CheckStockItemsResponse request = new CheckStockItemsResponse(responseList);
        getAgent().getContentManager().fillContent(msg, request);
        getAgent().send(msg);
    }


    // *************************************************************************
    // Asset functions
    // *************************************************************************
    /**
     * Process the asked list and compare with stock.
     * Return a list with items this warehouse can actually give.
     *
     * This function also lock the requested items.
     *
     * @return List of element warehouse can handle
     */
    private List<Item> processReceivedList(List<Item> received){
        //TODO Update: add actual lock process.
        for(Item i : received){
            i.setPrice(42);//TODO Update: atm, all items cost 42
        }
        //Atm, the warehouse will always be able to handle all the requested items.
        return received; //TODO Critique: to do
    }
}
