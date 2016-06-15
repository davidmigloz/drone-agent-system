package com.davidflex.supermarket.agents.behaviours.shop_agent;

import com.davidflex.supermarket.agents.shop.ShopAgent;
import com.davidflex.supermarket.gui.ShopAgentGuiActionsAdapter;
import com.davidflex.supermarket.ontologies.company.concepts.Warehouse;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Location;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Updates the data of the GUI every x seconds.
 * Used by ShopAgent.
 */
public class ShowDataBehaviour extends TickerBehaviour {

    private static final Logger logger = LoggerFactory.getLogger(ShowDataBehaviour.class);
    private static final int REFRESH_PERIOD = 500;

    private List<Location> warehouses;
    private List<Location> drones;
    private List<Location> customers;

    public ShowDataBehaviour(Agent a) {
        super(a, REFRESH_PERIOD);
        warehouses = new ArrayList<>();
        drones = new ArrayList<>();
        customers = new ArrayList<>();
    }

    @Override
    protected void onTick() {
        // Reset list
        warehouses.clear();
        drones.clear();
        customers.clear();
        // Get Warehouses
        warehouses.addAll(((ShopAgent) getAgent()).getWarehouses()
                .stream().map(Warehouse::getLocation).collect(Collectors.toList()));
        // Get Drones
        drones.addAll(((ShopAgent) getAgent()).getDrones().values());
        // Get Customers
        customers.addAll(((ShopAgent) getAgent()).getActiveOrders().values());
        // Update data
        //logger.info("Updating GUI info");
        ShopAgentGuiActionsAdapter.actionUpdateInfo(warehouses, drones, customers);
    }
}
