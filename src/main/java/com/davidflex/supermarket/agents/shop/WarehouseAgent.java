package com.davidflex.supermarket.agents.shop;

import com.davidflex.supermarket.agents.behaviours.warehouse_agent.RegisterBehaviour;
import com.davidflex.supermarket.ontologies.company.CompanyOntolagy;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WarehouseAgent extends Agent {

    private static final Logger logger = LoggerFactory.getLogger(ShopAgent.class);

    private Codec codec;
    private Ontology ontology;

    private int x;
    private int y;

    public WarehouseAgent() {
        codec = new SLCodec(0); // fipa-sl0
        try {
            ontology = CompanyOntolagy.getInstance();
        } catch (BeanOntologyException e) {
            logger.error("Ontology error!", e);
            doDelete();
        }
    }

    @Override
    protected void setup() {
        // Setup content manager
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);
        // Get arguments (order number, coordX, coordY)
        Object[] args = getArguments();
        String shopAgent = null;
        if (args != null && args.length == 3) {
            shopAgent = (String) args[0];
            x = Integer.parseInt((String) args[1]);
            y = Integer.parseInt((String) args[2]);
            logger.debug("Arg: {} {} {}", shopAgent, x, y);
        } else {
            logger.error("Agent " + getLocalName() + " - Incorrect number of arguments");
            doDelete();
        }
        // Register in ShopAgent
        addBehaviour(new RegisterBehaviour(this, shopAgent));
    }

    public Codec getCodec() {
        return codec;
    }

    public Ontology getOntology() {
        return ontology;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}