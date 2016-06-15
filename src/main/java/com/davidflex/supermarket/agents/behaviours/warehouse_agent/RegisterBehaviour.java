package com.davidflex.supermarket.agents.behaviours.warehouse_agent;

import com.davidflex.supermarket.agents.shop.WarehouseAgent;
import com.davidflex.supermarket.ontologies.company.actions.RegisterWarehouse;
import com.davidflex.supermarket.ontologies.company.concepts.Warehouse;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Location;
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

/**
 * The warehouse register itself in shopAgent.
 * User by WarehouseAgent.
 */
public class RegisterBehaviour extends OneShotBehaviour {

    private static final Logger logger = LoggerFactory.getLogger(RegisterBehaviour.class);

    private AID shopAgent;

    public RegisterBehaviour(Agent a, AID shopAgent) {
        super(a);
        this.shopAgent = shopAgent;
    }

    @Override
    public void action() {
        try {
            // Send message to ShopAgent to register warehouse
            logger.info("Registering in shopAgent...");
            // Prepare message
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setSender(getAgent().getAID());
            msg.addReceiver(shopAgent);
            msg.setLanguage(((WarehouseAgent) getAgent()).getCodec().getName());
            msg.setOntology(((WarehouseAgent) getAgent()).getOntology().getName());
            // Fill the content
            Warehouse w = new Warehouse(
                    getAgent().getAID(),
                    new Location(
                            ((WarehouseAgent) getAgent()).getLocation().getX(),
                            ((WarehouseAgent) getAgent()).getLocation().getY())
            );
            RegisterWarehouse request = new RegisterWarehouse(w);
            Action a = new Action(getAgent().getAID(), request);
            getAgent().getContentManager().fillContent(msg, a);
            // Send message
            getAgent().send(msg);

            // Get confirmation
            try {
                MessageTemplate mt = MessageTemplate.MatchSender(shopAgent);
                msg = getAgent().blockingReceive(mt);
                ContentElement ce = getAgent().getContentManager().extractContent(msg);
                if (ce instanceof Done) {
                    logger.info("Registered!");
                } else {
                    logger.error("Wrong message received.");
                }
            } catch (OntologyException | Codec.CodecException e) {
                logger.error("Error extracting msg.", e);
            }
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Error filling msg.", e);
        }
    }
}
