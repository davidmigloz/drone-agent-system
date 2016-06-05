package com.davidflex.supermarket.gui;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;
import javafx.collections.ObservableList;

/**
 * Interactions PersonalAgent <-> GUI.
 */
interface PersonalAgentGuiActions {

    void setItems(ObservableList<Item> items);

}
