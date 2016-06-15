package com.davidflex.supermarket.agents.shop;

import com.davidflex.supermarket.agents.behaviours.drone_agent.HandleDeliverBehaviour;
import com.davidflex.supermarket.ontologies.company.CompanyOntolagy;
import com.davidflex.supermarket.ontologies.company.CompanyOntolagyVocabulary;
import com.davidflex.supermarket.ontologies.company.concepts.Order;
import com.davidflex.supermarket.ontologies.company.concepts.Warehouse;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Item;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Location;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Drone agent.
 * A Drone is owned by a Warehouse and can carry one order.
 *
 * @since   June 10, 2016
 * @author  David Miguel Lozano
 * @author  Constantin MASSON
 */
public class DroneAgent extends Agent {

    private static final Logger logger = LoggerFactory.getLogger(DroneAgent.class);

    // Attributes Jade
    private Codec       codec;
    private Ontology    ontology;

    // Attributes drone
    private Warehouse   warehouse; //Owner of this drone
    private AID         shopAgent;
    private Location    position;
    private Order       order; //Order drone is in currently charge


    /**
     * Create a new drone without order.
     */
    public DroneAgent() {
        this.order      = null;
        this.codec      = new SLCodec(0); // fipa-sl0
        try {
            ontology = CompanyOntolagy.getInstance();
        } catch (BeanOntologyException e) {
            logger.error("Ontology error!", e);
            doDelete();
        }
    }

    @Override
    protected void setup() {
        //Process parameters
        try {
            Object[] args = getArguments();
            this.processParameters(args);
        } catch (Exception e) {
            logger.error("Unable to start drone agent: "+getLocalName()+
                    "\nUsage: shopName, warehouseName, x, y");
            doDelete();
        }
        // Setup content manager
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology, ShopOntology.ONTOLOGY_NAME);
        getContentManager().registerOntology(ontology, CompanyOntolagyVocabulary.ONTOLOGY_NAME);

        this.addBehaviour(new HandleDeliverBehaviour(this));
    }

    /**
     * Process the given parameters and set drone values.
     *
     * @param args          Array of parameters
     * @throws IllegalArgumentException If invalid parameters
     */
    private void processParameters(Object[] args) throws Exception {
        if(args == null || args.length != 4){
            throw new IllegalArgumentException("Invalid parameters for DroneAgent");
        }
        //Recover parameters from args
        String shopAgentName = (String) args[0];
        String warehouseName = (String) args[1];
        int x = Integer.parseInt((String) args[2]);
        int y = Integer.parseInt((String) args[3]);

        //Set drone values
        Location dronePos       = new Location(x, y);
        Location warehousePos   = new Location(x, y);
        AID warehouseAID        = new AID(warehouseName, AID.ISLOCALNAME);
        this.shopAgent          = new AID(shopAgentName, AID.ISLOCALNAME);
        this.warehouse          = new Warehouse(warehouseAID, warehousePos);
        this.position           = dronePos;

        logger.debug("Drone successfully created from parameters");
    }


    // *************************************************************************
    // Getters - Setters
    // *************************************************************************
    public Codec getCodec() {
        return codec;
    }

    public String getShopOntologyName() {
        return ShopOntologyVocabulary.ONTOLOGY_NAME;
    }

    public Ontology getCompanyOntology() {
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