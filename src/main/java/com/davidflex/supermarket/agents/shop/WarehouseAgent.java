package com.davidflex.supermarket.agents.shop;

import com.davidflex.supermarket.agents.behaviours.warehouse_agent.ListenCheckStockRequest;
import com.davidflex.supermarket.agents.behaviours.warehouse_agent.RegisterBehaviour;
import com.davidflex.supermarket.agents.behaviours.warehouse_agent.SetupFleetBehavior;
import com.davidflex.supermarket.agents.utils.JadeUtils;
import com.davidflex.supermarket.ontologies.company.CompanyOntolagy;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Item;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.wrapper.ContainerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * WarehouseAgent.
 * A warehouse has a stock of items and a fleet of drones.
 * Parameters: {shopAgent, coorX, coorY, [fleetSize]}
 *
 * @since   9 June, 2016
 * @author  Constantin MASSON
 * @author  David Miguel Lozano
 */
public class WarehouseAgent extends Agent {

    //Constants
    private static final Logger logger = LoggerFactory.getLogger(ShopAgent.class);
    private static int FLEET_SIZE_MIN       = 2;
    private static int FLEET_SIZE_MAX       = 42;
    private static int FLEET_SIZE_DEFAULT   = 5;

    //Attributes Jade
    private Codec               codec;
    private Ontology            ontology;
    private ContainerController container;

    //Warehouse Attributes
    private int         x;
    private int         y;
    private int         fleetSize;
    private List<AID>   fleet;
    private List<Item>  stock; //List of items in stock (Item got its quantity)


    // *************************************************************************
    // setup - Initialization
    // *************************************************************************
    public WarehouseAgent() {
        this.fleet = new ArrayList<>(FLEET_SIZE_DEFAULT);
        this.codec = new SLCodec(0); // fipa-sl0
        try {
            ontology = CompanyOntolagy.getInstance();
        } catch (BeanOntologyException ex) {
            logger.error("Unable to set the Company ontology in warehouseAgent!", ex);
            doDelete();
        }
    }

    @Override
    protected void setup() {
        // Setup content manager
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);
        String shopAgent = null;
        try {
            // Get arguments (order number, coordX, coordY, [drones])
            Object[] args = getArguments();
            shopAgent = this.processParameters(args);
        } catch (Exception e) {
            logger.error("Unable to start agent: "+getLocalName()+
                    "\nUsage: shopName, x, y, [drones]" +
                    "\nDrones value between "+FLEET_SIZE_MIN+" and "+FLEET_SIZE_MAX);
            doDelete();
        }
        //Create the container for this warehouse
        this.container = JadeUtils.createContainer("warehouse-"+x+"-"+y);
        // Register in ShopAgent
        this.addBehaviour(new RegisterBehaviour(this, shopAgent));
        this.addBehaviour(new SetupFleetBehavior(this));
        this.addBehaviour(new ListenCheckStockRequest(this));
    }

    /**
     * Process the given parameters and return the ShopAgent name.
     *
     * @param args          Array of parameters
     * @return              The ShopAgent name
     * @throws Exception    If invalid parameters
     */
    private String processParameters(Object[] args) throws Exception {
        if(args == null || (args.length != 3 && args.length != 4)){
            throw new Exception("Invalid parameters for WarehouseAgent");
        }
        String shopAgent = (String) args[0];
        //TODO update: att test x,y value integrity (Is on the map etc)
        this.x = Integer.parseInt((String) args[1]);
        this.y = Integer.parseInt((String) args[2]);
        this.fleetSize = FLEET_SIZE_DEFAULT;
        if(args.length == 4){
            this.fleetSize = Integer.parseInt((String)args[3]);
            if(this.fleetSize < FLEET_SIZE_MIN || this.fleetSize > FLEET_SIZE_MAX){
                throw new IllegalArgumentException("Invalid parameters for WarehouseAgent");
            }
        }
        logger.debug("Arg: {} {} {} {}", shopAgent, x, y, fleetSize);
        return shopAgent;
    }


    // *************************************************************************
    // Getters - Setters
    // *************************************************************************
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

    public int getFleetSize(){
        return this.fleetSize;
    }

    public List<AID> getFleet(){
        return this.fleet;
    }

    public ContainerController getContainer(){
        return this.container;
    }
}