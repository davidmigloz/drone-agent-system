package com.davidflex.supermarket.agents.shop;

import com.davidflex.supermarket.ontologies.company.CompanyOntolagy;
import com.davidflex.supermarket.ontologies.company.concepts.Order;
import com.davidflex.supermarket.ontologies.company.concepts.Warehouse;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Location;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DroneAgent extends Agent {

    private static final Logger logger = LoggerFactory.getLogger(DroneAgent.class);

    // Attributes Jade
    private Codec codec;
    private Ontology ontology;

    // Attributes drone
    private Warehouse warehouse;
    private AID shopAgent;
    private Location position;
    private Order order;


    public DroneAgent() {
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
    }

    public Codec getCodec() {
        return codec;
    }

    public Ontology getOntology() {
        return ontology;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Location getPosition() {
        return position;
    }

    public void setPosition(Location position) {
        this.position = position;
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