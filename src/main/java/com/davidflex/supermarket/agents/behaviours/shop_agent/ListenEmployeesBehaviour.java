package com.davidflex.supermarket.agents.behaviours.shop_agent;

import com.davidflex.supermarket.agents.shop.ShopAgent;
import com.davidflex.supermarket.ontologies.company.actions.RegisterWarehouse;
import com.davidflex.supermarket.ontologies.company.actions.UnregisterDrone;
import com.davidflex.supermarket.ontologies.company.predicates.GetListWarehousesRequest;
import com.davidflex.supermarket.ontologies.company.predicates.GetListWarehousesResponse;
import com.davidflex.supermarket.ontologies.company.predicates.InformPosition;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listen to Warehouses and Drones messages.
 * Used by ShopAgent.
 */
public class ListenEmployeesBehaviour extends CyclicBehaviour {

    private static final Logger logger = LoggerFactory.getLogger(ListenEmployeesBehaviour.class);

    private MessageTemplate mtOnto;

    public ListenEmployeesBehaviour(Agent a) {
        super(a);
        mtOnto = MessageTemplate.MatchOntology(((ShopAgent) getAgent()).getCompanyOntology().getName());
    }

    @Override
    public void action() {
        ACLMessage msg = getAgent().receive(mtOnto);
        if (msg != null) {
            try {
                ContentElement ce = getAgent().getContentManager().extractContent(msg);
                if (ce instanceof Action) {
                    Action a = (Action) ce;
                    if (a.getAction() instanceof RegisterWarehouse) {
                        // Register warehouse
                        logger.info("Registering warehouse: " + msg.getSender().getLocalName());
                        RegisterWarehouse rw = (RegisterWarehouse) a.getAction();
                        ((ShopAgent) getAgent()).registerWarehouse(rw.getWarehouse());
                        sendDone(msg.getSender(), a);
                    } else if (a.getAction() instanceof UnregisterDrone) {
                        // Unregistering drone
                        logger.info("Unregistering drone: " + msg.getSender().getLocalName());
                        UnregisterDrone ud = (UnregisterDrone) a.getAction();
                        // Remove drone
                        ((ShopAgent) getAgent()).unregisterDrone(ud.getDrone());
                        // Remove order
                        ((ShopAgent) getAgent()).getActiveOrders().remove(ud.getOrderId());
                    }
                } else if (ce instanceof GetListWarehousesRequest) {
                    // Send list of warehouses
                    logger.info("Sending list of warehouses to " + msg.getSender().getLocalName());
                    sendListWarehouses(msg.getSender());
                } else if (ce instanceof InformPosition) {
                    // Register position of drone
                    InformPosition ip = (InformPosition) ce;
                    logger.info("Drone " + msg.getSender().getLocalName() + "is "
                            + ip.getPosition().toString());
                    ((ShopAgent) getAgent()).setDronePosition(ip.getDrone(), ip.getPosition());
                } else {
                    logger.error("Wrong message received.");
                }
            } catch (Codec.CodecException | OntologyException e) {
                logger.error("Error at extracting message", e);
            }
        } else {
            block();
        }
    }

    /**
     * Sends list of available products to the customer.
     */
    private void sendDone(AID employee, Action action) {
        try {
            // Prepare message
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setSender(getAgent().getAID());
            msg.addReceiver(employee);
            msg.setLanguage(((ShopAgent) getAgent()).getCodec().getName());
            msg.setOntology(((ShopAgent) getAgent()).getCompanyOntology().getName());
            // Fill the content
            Done d = new Done(action);
            getAgent().getContentManager().fillContent(msg, d);
            // Send message
            getAgent().send(msg);
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Error filling msg.", e);
        }
    }

    private void sendListWarehouses(AID employee) {
        try {
            // Prepare message
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setSender(getAgent().getAID());
            msg.addReceiver(employee);
            msg.setLanguage(((ShopAgent) getAgent()).getCodec().getName());
            msg.setOntology(((ShopAgent) getAgent()).getCompanyOntology().getName());
            // Fill the content
            GetListWarehousesResponse response = new GetListWarehousesResponse(
                    ((ShopAgent) getAgent()).getWarehouses());
            getAgent().getContentManager().fillContent(msg, response);
            // Send message
            getAgent().send(msg);
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Error filling msg.", e);
        }
    }
}
