package com.davidflex.supermarket.agents.shop;

import com.davidflex.supermarket.agents.behaviours.ListenNewOrdersBehaviour;
import com.davidflex.supermarket.agents.utils.DFUtils;
import com.davidflex.supermarket.ontologies.company.CompanyOntolagy;
import com.davidflex.supermarket.ontologies.ecommerce.ECommerceOntology;
import com.davidflex.supermarket.ontologies.ecommerce.ECommerceOntologyVocabulary;
import jade.content.lang.Codec;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.domain.FIPAException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ShopAgent extends Agent {

    private static final Logger logger = LoggerFactory.getLogger(ShopAgent.class);

    private Codec codec;
    private Ontology companyOntolagy;
    private Ontology eCommerceOntology;

    private AtomicLong orderNum;
    private Map activeOrders;

    public ShopAgent() {
        orderNum = new AtomicLong();
        activeOrders = new HashMap<>();
        try {
            companyOntolagy = CompanyOntolagy.getInstance();
            eCommerceOntology = ECommerceOntology.getInstance();
        } catch (BeanOntologyException e) {
            logger.error("Ontology error!", e);
            doDelete();
        }
    }

    @Override
    protected void setup() {
        // Register in DF
        try {
            DFUtils.registerInDF(this, ECommerceOntologyVocabulary.SHOP_NAME,
                    ECommerceOntologyVocabulary.SHOP_TYPE);
        } catch (FIPAException e) {
            logger.error("Error at registering in DF", e);
        }
        // Add behaviours
        addBehaviour(new ListenNewOrdersBehaviour(this));
    }

    @Override
    protected void takeDown() {
        try {
            DFUtils.deregisterFromDF(this);
        } catch (FIPAException e) {
            logger.error("Error at deregistering in DF", e);
        }
    }

    public Ontology getCompanyOntolagy() {
        return companyOntolagy;
    }

    public Ontology getECommerceOntology() {
        return eCommerceOntology;
    }
}