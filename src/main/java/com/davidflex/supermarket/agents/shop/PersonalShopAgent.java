package com.davidflex.supermarket.agents.shop;

import com.davidflex.supermarket.agents.behaviours.personal_shop_agent.GetOrderBehaviour;
import com.davidflex.supermarket.ontologies.company.CompanyOntolagy;
import com.davidflex.supermarket.ontologies.company.CompanyOntolagyVocabulary;
import com.davidflex.supermarket.ontologies.company.concepts.Order;
import com.davidflex.supermarket.ontologies.shop.ShopOntology;
import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonalShopAgent extends Agent {

    private static final Logger logger = LoggerFactory.getLogger(PersonalShopAgent.class);

    private Codec codec;
    private Ontology ontology;

    private AID shopAgent;
    private Order order;

    public PersonalShopAgent() {
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
        getContentManager().registerOntology(ontology, ShopOntology.ONTOLOGY_NAME);
        getContentManager().registerOntology(ontology, CompanyOntolagyVocabulary.ONTOLOGY_NAME);
        // Add behaviours
        addBehaviour(new GetOrderBehaviour(this));
    }

    public Codec getCodec() {
        return codec;
    }

    public String getShopOntologyName() {
        return ShopOntologyVocabulary.ONTOLOGY_NAME;
    }

    public Ontology getCompanyOntology() {
        return ontology;
    }

    public AID getShopAgent() {
        return shopAgent;
    }

    public void setShopAgent(AID shopAgent) {
        this.shopAgent = shopAgent;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}