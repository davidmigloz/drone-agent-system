package com.davidflex.supermarket.agents.shop;

import com.davidflex.supermarket.agents.behaviours.personal_shop_agent.GetOrderBehaviour;
import com.davidflex.supermarket.ontologies.company.CompanyOntolagy;
import com.davidflex.supermarket.ontologies.company.elements.Order;
import com.davidflex.supermarket.ontologies.shop.ShopOntology;
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
    private Ontology shopOntology;
    private Ontology companyOntology;

    private AID shopAgent;
    private Order order;

    public PersonalShopAgent() {
        codec = new SLCodec(0); // fipa-sl0
        try {
            shopOntology = ShopOntology.getInstance();
            companyOntology = CompanyOntolagy.getInstance();
        } catch (BeanOntologyException e) {
            logger.error("Ontology error!", e);
            doDelete();
        }
    }

    @Override
    protected void setup() {
        // Setup content manager
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(shopOntology);
        getContentManager().registerOntology(companyOntology);
        // Add behaviours
        addBehaviour(new GetOrderBehaviour(this));
    }

    public Codec getCodec() {
        return codec;
    }

    public Ontology getShopOntology() {
        return shopOntology;
    }

    public Ontology getCompanyOntology() {
        return companyOntology;
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