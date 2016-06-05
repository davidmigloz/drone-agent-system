package com.davidflex.supermarket.gui;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Interactions PersonalAgent <-> GUI.
 */
public class PersonalAgentGuiActionsAdapter {

    private static final Logger logger = LoggerFactory.getLogger(PersonalAgentGuiActionsAdapter.class);

    private static List<PersonalAgentGuiActions> instances = new ArrayList<>();

    public PersonalAgentGuiActionsAdapter() {
        throw new IllegalStateException("Not instantiable.");
    }

    static void setInstance(PersonalAgentGuiActions instance) {
        logger.debug("New instance");
        PersonalAgentGuiActionsAdapter.instances.add(instance);
    }

    public static boolean isInstanceNull(int n) {
        return instances.size() != n + 1;
    }

    public static void actionSetItems(int n, ObservableList<Item> items) {
        logger.debug("Set items instance " + n);
        instances.get(n).setItems(items);
    }
}
