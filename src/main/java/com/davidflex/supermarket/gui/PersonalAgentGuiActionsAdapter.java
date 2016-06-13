package com.davidflex.supermarket.gui;

import com.davidflex.supermarket.ontologies.ecommerce.concepts.Item;
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

    /**
     * Check if an instace with given id exists.
     *
     * @param n number of instance
     */
    public static boolean isInstanceNull(int n) {
        return instances.size() != n + 1;
    }

    /**
     * Set order items. To be displayed in the table.
     *
     * @param n number of instance
     * @param items order items
     */
    public static void actionSetItems(int n, ObservableList<Item> items) {
        logger.debug("Set items instance " + n);
        instances.get(n).setItems(items);
    }

    /**
     * Append a message in status text area.
     *
     * @param n number of instance
     * @param msg message to display (without line break)
     */
    public static void actionAppendStatusMsg(int n, String msg) {
        instances.get(n).appendStatusMsg(msg);
    }
}
