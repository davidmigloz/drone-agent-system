package com.davidflex.supermarket.agents.behaviours.warehouse_agent;

import com.davidflex.supermarket.agents.shop.WarehouseAgent;
import com.davidflex.supermarket.ontologies.company.actions.AssignOrder;
import com.davidflex.supermarket.ontologies.company.concepts.Order;
import com.davidflex.supermarket.ontologies.company.predicates.ConfirmPurchaseRequest;
import com.davidflex.supermarket.ontologies.company.predicates.ConfirmPurchaseResponse;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


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


    // *************************************************************************
    // Core functions
    // *************************************************************************
    @Override
    public void action() {
        logger.info("Start ListenConfirmPurchaseRequest behavior");

        ConfirmPurchaseRequest request;
        try {
            request = this.receiveConfirmPurchaseRequest();
            //If null, means no msg received and behavior blocked. (Must quit now)
            if(request == null){
                block();
                return;
            }
            logger.info("ConfirmPurchaseRequest received.");
        } catch (Exception e) {
            logger.error("Unable to receive ConfirmPurchaseRequest message", e);
            //TODO Update (Important) Send error
            return;
        }

        //Select a drone available or error message if no available.
        logger.info("Request a drone for this order");
        AID droneAID = this.requestDrone();
        if(droneAID == null){
            logger.error("No drone available for the order:"+request.getOrder().getId());
            //TODO Update (Important) Send error
        }
        logger.info("Drone ("+droneAID+") set to order: "+request.getOrder().getId());


        logger.info("Update stock in warehouse according to the request");
        //TODO Update: update the actual stock in warehouse (Atm, use infinite)
        logger.info("Stock updated successfully");

        //Recover the order and send it to the drone
        Order order = request.getOrder();
        // DEV NOTE:
        // in request, order is the original order and item the list drone
        // must actually deal with.
        // Since in the assignOrder to drone, the order is altered with only
        // the items for the drone, we must here change the order inside list
        // with the actual list he is supposed to deal with.
        order.setItems(request.getItems()); //See DEV NOTE above.
        try {
            this.sendOrderToDrone(order, droneAID);
        } catch (Codec.CodecException | OntologyException ex) {
            logger.error("Error while sending the order to the drone");
            //TODO Update (Important): Error to handle
        }
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
        MessageTemplate mt  = MessageTemplate.MatchPerformative(ACLMessage.CONFIRM);
        ACLMessage      msg = this.getAgent().receive(mt);
        if(msg == null){
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

        //TODO Update: add a check if the ConfirmPurchaseRequest is well for this warehouse.
        //(In the request, there is a 'warehouse' attributes, it should be the
        //same as current getAgent().getAID() (Test the localName)
        //Check whether received message was actually destined to this warehouse.
        return ((ConfirmPurchaseRequest) ce);
    }

    /**
     * Send the order to the drone (Assign it)
     *
     * @param order     Order to assign to the drone
     * @param droneAID  Drone to use
     */
    private void sendOrderToDrone(Order order, AID droneAID)
            throws Codec.CodecException, OntologyException {
        //Create message
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setSender(getAgent().getAID());
        msg.addReceiver(droneAID);
        msg.setLanguage(((WarehouseAgent) getAgent()).getCodec().getName());
        msg.setOntology(((WarehouseAgent) getAgent()).getOntology().getName());
        //Fill msg content and send it like a boss
        AssignOrder assignOrder = new AssignOrder(order);
        Action action = new Action(getAgent().getAID(), assignOrder);
        getAgent().getContentManager().fillContent(msg, action);
        getAgent().send(msg);
    }


    // *************************************************************************
    // Asset functions
    // *************************************************************************

    /**
     * Check from the warehouse list which drone is available.
     *
     * @return Drone luckily selected or null if any available
     */
    private AID requestDrone(){
        //Check each drone if available
        for(Map.Entry<AID, Boolean> map : ((WarehouseAgent)getAgent()).getFleet().entrySet()){
            //If drone is available, take it and change its state
            if(map.getValue()){
                map.setValue(false); //No more available
                return map.getKey();
            }
        }
        return null;
    }
}
