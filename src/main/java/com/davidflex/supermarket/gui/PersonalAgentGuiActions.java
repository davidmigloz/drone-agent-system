package com.davidflex.supermarket.gui;

import com.davidflex.supermarket.ontologies.ecommerce.concepts.Item;
import javafx.collections.ObservableList;

/**
 * Interactions PersonalAgent <-> GUI.
 */
interface PersonalAgentGuiActions {

    /**
     * Set order items. To be displayed in the table.
     */
    void setItems(ObservableList<Item> items);

    /**
     * Append a message in status text area.
     */
    void appendStatusMsg(String msg);
}
