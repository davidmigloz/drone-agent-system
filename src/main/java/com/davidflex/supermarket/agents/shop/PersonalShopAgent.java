package com.davidflex.supermarket.agents.shop;

import com.davidflex.supermarket.agents.behaviours.GetOrderInfoBehaviour;
import com.davidflex.supermarket.agents.utils.DFUtils;
import com.davidflex.supermarket.ontologies.company.CompanyOntolagy;
import com.davidflex.supermarket.ontologies.company.elements.Order;
import com.davidflex.supermarket.ontologies.ecommerce.ECommerceOntology;
import com.davidflex.supermarket.ontologies.ecommerce.ECommerceOntologyVocabulary;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.domain.FIPAException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonalShopAgent extends Agent {

    private static final Logger logger = LoggerFactory.getLogger(PersonalShopAgent.class);

    private Codec codec;
    private Ontology companyOntolagy;
    private Ontology eCommerceOntology;
    private Order order;

    public PersonalShopAgent() {
        codec = new SLCodec(0); // fipa-sl0
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
        // Setup content manager
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(eCommerceOntology);
        getContentManager().registerOntology(companyOntolagy);
        // Register in DF
        try {
            DFUtils.registerInDF(this, ECommerceOntologyVocabulary.SHOP_NAME,
                    ECommerceOntologyVocabulary.SHOP_TYPE);
        } catch (FIPAException e) {
            logger.error("Error at registering in DF", e);
            doDelete();
        }
        // Add behaviours
        addBehaviour(new GetOrderInfoBehaviour(this));
    }

    public Codec getCodec() {
        return codec;
    }

    public Ontology getCompanyOntolagy() {
        return companyOntolagy;
    }

    public Ontology getECommerceOntology() {
        return eCommerceOntology;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}