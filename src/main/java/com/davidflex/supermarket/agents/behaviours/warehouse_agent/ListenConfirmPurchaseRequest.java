package com.davidflex.supermarket.agents.behaviours.warehouse_agent;

import com.davidflex.supermarket.agents.shop.WarehouseAgent;
import com.davidflex.supermarket.ontologies.company.predicates.ConfirmPurchaseRequest;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listen for incoming ConfirmPurchaseRequest request and handle it.
 *
 * ConfirmPurchaseRequest is meant to be sent by personalShopAgent in order
 * to send the final list of item to purchase.
 *
 * @since   June 14, 2016
 * @author  Constantin MASSON
 */
public class ListenConfirmPurchaseRequest extends CyclicBehaviour{
    private static final Logger logger = LoggerFactory.getLogger(ListenConfirmPurchaseRequest.class);

    public ListenConfirmPurchaseRequest(Agent agent){
        super(agent);
    }

    @Override
    public void action() {
        logger.info("Start ListenConfirmPurchaseRequest behavior");

        ConfirmPurchaseRequest request;
        try {
            request = this.receiveConfirmPurchaseRequest();
            //If null, means no msg received and behavior blocked. (Must quit now)
            if(request == null){
                return;
            }
            logger.info("ConfirmPurchaseRequest received.");
        } catch (Exception e) {
            logger.error("Unable to receive ConfirmPurchaseRequest message");
            return;
        }

        //TODO Critique: Check if drone available and assign

        logger.info("Update stock in warehouse according to the request");
        this.updateStock(request);
        logger.info("Stock updated successfully");
    }


    // *************************************************************************
    // Send / Receive functions
    // *************************************************************************

    /**
     * Check whether a CONFIRM message has been received and process it.
     * Process the message and return the received ConfirmPurchaseRequest.
     * CONFIRM message should be a ConfirmPurchaseRequest content, otherwise, exception.
     *
     * @return                          The received ConfirmPurchaseRequest
     * @throws Codec.CodecException     If unable to recover the message
     * @throws OntologyException        If unable to recover the message
     * @throws Exception                If invalid message received
     */
    private ConfirmPurchaseRequest receiveConfirmPurchaseRequest() throws Exception {
        //Message should be a request with valid ontology
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
                MessageTemplate.MatchOntology(
                        ((WarehouseAgent)getAgent()).getOntology().getName()
                )
        );
        ACLMessage msg = this.getAgent().receive(mt);
        if(msg == null){
            block();
            return null;
        }

        //Process content if message received
        ContentElement ce = this.getAgent().getContentManager().extractContent(msg);
        //Message should be CheckStockItemsRequest
        if(!(ce instanceof ConfirmPurchaseRequest)){
            logger.error("Confirm message received but is not ConfirmPurchaseRequest");
            throw new Exception(
                    "Message received is a CONFIRM but should be ConfirmPurchaseRequest"
            );
        }
        ConfirmPurchaseRequest c = ((ConfirmPurchaseRequest) ce);

        //Check whether received message was actually destined to this warehouse.
        if(c.getWarehouse().getWarehouseAgent() != this.getAgent().getAID()){
            logger.error("ConfirmPurchaseRequest received with wrong AID");
            throw new Exception("ConfirmPurchaseRequest received with wrong AID");
        }
        return (ConfirmPurchaseRequest) ce;
    }


    // *************************************************************************
    // Asset functions
    // *************************************************************************
    private void updateStock(ConfirmPurchaseRequest request){
        //TODO Update (Critique): to implements
        //Probably add exception in case of error
    }
}
