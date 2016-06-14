package com.davidflex.supermarket.gui;

import com.davidflex.supermarket.ontologies.ecommerce.concepts.Location;

import java.util.List;

/**
 * Interactions ShopAgent <-> GUI.
 */
public class ShopAgentGuiActionsAdapter {

    private static ShopAgentGuiActions instance;

    public ShopAgentGuiActionsAdapter() {
        throw new IllegalStateException("Not instantiable.");
    }

    static void setInstance(ShopAgentGuiActions instance) {
        ShopAgentGuiActionsAdapter.instance = instance;
    }

    public static void actionUpdateInfo (List<Location> warehouses, List<Location> drones,
                                    List<Location> customers) {
        if(instance != null) {
            instance.updateInfo(warehouses, drones, customers);
        }
    }
}
