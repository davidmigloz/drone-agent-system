package com.davidflex.supermarket.gui;

import com.davidflex.supermarket.ontologies.company.concepts.Position;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Location;

import java.util.List;

/**
 * Interactions ShopAgent <-> GUI.
 */
interface ShopAgentGuiActions {

    /**
     * Updates the gui information with the new data.
     */
    void updateInfo(List<Position> warehouses, List<Position> drones, List<Location> customers);
}
