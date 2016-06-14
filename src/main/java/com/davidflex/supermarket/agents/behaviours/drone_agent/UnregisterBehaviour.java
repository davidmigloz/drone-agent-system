package com.davidflex.supermarket.agents.behaviours.drone_agent;

import com.davidflex.supermarket.agents.shop.DroneAgent;
import com.davidflex.supermarket.ontologies.company.actions.UnregisterDrone;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unregister drone from shopAgent (to not appear in the map anymore).
 * Used by droneAgent.
 */
class UnregisterBehaviour extends OneShotBehaviour {

    private static final Logger logger = LoggerFactory.getLogger(UnregisterBehaviour.class);

    @Override
    public void action() {
        try {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setSender(getAgent().getAID());
            msg.addReceiver(((DroneAgent) getAgent()).getShopAgent());
            msg.setLanguage(((DroneAgent) getAgent()).getCodec().getName());
            msg.setOntology(((DroneAgent) getAgent()).getOntology().getName());
            // Fill the content
            UnregisterDrone ud = new UnregisterDrone(getAgent().getAID());
            Action a = new Action(getAgent().getAID(), ud);
            getAgent().getContentManager().fillContent(msg, a);
            // Send message
            getAgent().send(msg);
        } catch (Codec.CodecException | OntologyException e) {
            logger.error("Error at sending UnregisterDrone.", e);
        }
    }
}
