package com.davidflex.supermarket.agents.shop;

import com.davidflex.supermarket.agents.behaviours.shop_agent.ListenEmployeesBehaviour;
import com.davidflex.supermarket.agents.behaviours.shop_agent.ListenNewOrdersBehaviour;
import com.davidflex.supermarket.agents.behaviours.shop_agent.ShowDataBehaviour;
import com.davidflex.supermarket.agents.utils.DFUtils;
import com.davidflex.supermarket.agents.utils.JadeUtils;
import com.davidflex.supermarket.ontologies.company.CompanyOntolagy;
import com.davidflex.supermarket.ontologies.company.elements.Position;
import com.davidflex.supermarket.ontologies.company.elements.Warehouse;
import com.davidflex.supermarket.ontologies.ecommerce.ECommerceOntologyVocabulary;
import com.davidflex.supermarket.ontologies.ecommerce.elements.Location;
import com.davidflex.supermarket.ontologies.shop.ShopOntology;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAException;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ShopAgent extends Agent {

    private static final Logger logger = LoggerFactory.getLogger(ShopAgent.class);

    private Codec codec;
    private Ontology shopOntology;
    private Ontology companyOntology;
    private ContainerController container;

    private List<Warehouse> warehouses;
    private AtomicLong orderIDs;
    private Map<Long, Location> activeOrders;
    private Map<AID, Position> drones;


    public ShopAgent() {
        codec = new SLCodec(0); // fipa-sl0
        try {
            shopOntology = ShopOntology.getInstance();
            companyOntology = CompanyOntolagy.getInstance();
        } catch (BeanOntologyException e) {
            logger.error("Ontology error!", e);
            doDelete();
        }
        container = JadeUtils.createContainer("personalShopAgents");
        warehouses = new ArrayList<>();
        orderIDs = new AtomicLong();
        activeOrders = new HashMap<>();
        drones = new HashMap<>();
    }

    @Override
    protected void setup() {
        // Setup content manager
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(shopOntology);
        getContentManager().registerOntology(companyOntology);
        // Register in DF
        try {
            DFUtils.registerInDF(this, ECommerceOntologyVocabulary.SHOP_NAME,
                    ECommerceOntologyVocabulary.SHOP_TYPE);
        } catch (FIPAException e) {
            logger.error("Error at registering in DF", e);
            doDelete();
        }

        // Launch GUI
        try {
            getContainerController().createNewAgent("ShopAgentGUI",
                    ShopAgentGuiAgent.class.getName(), new Object[]{});
        } catch (StaleProxyException e) {
            logger.error("Error launching GUI", e);
        }

        // Add behaviours
        addBehaviour(new ListenNewOrdersBehaviour(this));
        addBehaviour(new ListenEmployeesBehaviour(this));
        addBehaviour(new ShowDataBehaviour(this, 2000));
    }

    @Override
    protected void takeDown() {
        try {
            DFUtils.deregisterFromDF(this);
        } catch (FIPAException e) {
            logger.error("Error at deregistering in DF", e);
        }
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

    public ContainerController getContainer() {
        return container;
    }

    /**
     * Register a new order.
     *
     * @param location customer location
     * @return orderID
     */
    public long addNewOrder(Location location) {
        long num = orderIDs.incrementAndGet();
        activeOrders.put(num, location);
        return num;
    }

    public Map<Long, Location> getActiveOrders() {
        return activeOrders;
    }

    public void registerWarehouse(Warehouse warehouse) {
        warehouses.add(warehouse);
    }

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setDronePosition(AID drone, Position position){
        drones.put(drone, position);
    }

    public void unregisterDrone(AID drone) {
        drones.remove(drone);
    }

    public Map<AID, Position> getDrones() {
        return drones;
    }
}