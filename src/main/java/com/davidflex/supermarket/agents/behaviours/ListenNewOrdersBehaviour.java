package com.davidflex.supermarket.agents.behaviours;

import com.davidflex.supermarket.agents.shop.ShopAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listen the ContactRequest from PersonaAgents and manages them.
 * Used by ShopAgent.
 */
public class ListenNewOrdersBehaviour extends CyclicBehaviour {

    private static final Logger logger = LoggerFactory.getLogger(ListenNewOrdersBehaviour.class);

    private MessageTemplate mt;

    public ListenNewOrdersBehaviour(Agent a) {
        super(a);
        mt = MessageTemplate.MatchOntology(((ShopAgent) myAgent).getECommerceOntology().getName());
    }

    @Override
    public void action() {
        ACLMessage mensaje = myAgent.receive(mt);
        if (mensaje!= null) {
            logger.info("New ContactRequest received!");


        }else{
            block();
        }
    }
}
