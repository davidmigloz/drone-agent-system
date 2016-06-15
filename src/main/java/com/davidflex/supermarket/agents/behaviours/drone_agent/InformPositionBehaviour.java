package com.davidflex.supermarket.agents.behaviours.drone_agent;

import com.davidflex.supermarket.agents.shop.DroneAgent;
import com.davidflex.supermarket.ontologies.company.predicates.InformPosition;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Location;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Communicates drone's possition to shopAgent.
 * Used by droneAgent.
 */
class InformPositionBehaviour extends TickerBehaviour {

    private static final Logger logger = LoggerFactory.getLogger(TickerBehaviour.class);
    private static final int PERIOD = 1000;

    private ACLMessage msg;

    InformPositionBehaviour(Agent a) {
        super(a, PERIOD);
        // Create message
        msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setSender(getAgent().getAID());
        msg.addReceiver(((DroneAgent) getAgent()).getShopAgent());
        msg.setLanguage(((DroneAgent) getAgent()).getCodec().getName());
        msg.setOntology(((DroneAgent) getAgent()).getCompanyOntology().getName());
    }

    @Override
    protected void onTick() {
        try {
            logger.info("Sending possition...");
            // Write possition
            Location position = ((DroneAgent) getAgent()).getPosition();
            InformPosition ip = new InformPosition(getAgent().getAID(), position);
            getAgent().getContentManager().fillContent(msg, ip);
            // Send message
            getAgent().send(msg);
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Error at sending InformPosition.", e);
        }
    }
}
