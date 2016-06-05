package com.davidflex.supermarket.agents.customer;

import com.davidflex.supermarket.gui.CustomerGuiController;
import com.davidflex.supermarket.gui.CustomerGuiActionsAdapter;
import com.davidflex.supermarket.gui.PersonalAgentGuiActionsAdapter;
import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;
import jade.core.Agent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class PersonalAgent extends Agent {

    private static final Logger logger = LoggerFactory.getLogger(CustomerGuiController.class);

    private int orderNumber;
    private int x;
    private int y;
    private ObservableList<Item> items;

    @SuppressWarnings({"unchecked", "StatementWithEmptyBody"})
    @Override
    protected void setup() {
        // Get arguments (order number)
        Object[] args = getArguments();
        if (args != null && args.length == 3) {
            orderNumber = Integer.parseInt((String) args[0]);
            x = Integer.parseInt((String) args[1]);
            y = Integer.parseInt((String) args[2]);
            logger.debug("Arg: {} {} {}", orderNumber, x, y);
        } else {
            logger.error("Agent " + getLocalName() + " - Incorrect number of arguments");
            doDelete();
        }
        // Read items to buy
        try(FileInputStream fin = new FileInputStream("order" + orderNumber + ".txt");
            ObjectInputStream ois = new ObjectInputStream(fin)) {
            List<Item> list = (List<Item>) ois.readObject();
            items = FXCollections.observableList(list);
        } catch (ClassNotFoundException | IOException e) {
            logger.error("Cannot read order file", e);
        }
        logger.debug(items.size() + " items read");
        // Open window
        CustomerGuiActionsAdapter.actionCreateNewWindow("Personal Agent - Order #" + orderNumber);
        // Set items
        while(PersonalAgentGuiActionsAdapter.isInstanceNull(orderNumber)) {}
        PersonalAgentGuiActionsAdapter.actionSetItems(orderNumber, items);
    }
}