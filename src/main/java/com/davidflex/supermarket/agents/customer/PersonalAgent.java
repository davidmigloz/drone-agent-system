package com.davidflex.supermarket.agents.customer;

import com.davidflex.supermarket.agents.behaviours.personal_agent.ContactBehaviour;
import com.davidflex.supermarket.gui.CustomerGuiActionsAdapter;
import com.davidflex.supermarket.gui.PersonalAgentGuiActionsAdapter;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Item;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Location;
import com.davidflex.supermarket.ontologies.shop.ShopOntology;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;
import jade.core.Agent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class PersonalAgent extends Agent {

    private static final Logger logger = LoggerFactory.getLogger(PersonalAgent.class);

    private Codec codec;
    private Ontology ontology;

    private int orderNumber;
    private long orderId;
    private Location location;
    private ObservableList<Item> items;

    public PersonalAgent() {
        codec = new SLCodec(0); // fipa-sl0
        try {
            ontology = ShopOntology.getInstance();
        } catch (BeanOntologyException e) {
            logger.error("Ontology error!", e);
            doDelete();
        }
    }

    @SuppressWarnings({"unchecked", "StatementWithEmptyBody"})
    @Override
    protected void setup() {
        // Setup content manager
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);
        // Get arguments (order number, coordX, coordY)
        Object[] args = getArguments();
        if (args != null && args.length == 3) {
            orderNumber = Integer.parseInt((String) args[0]);
            location = new Location(Integer.parseInt((String) args[1]), Integer.parseInt((String) args[2]));
            logger.debug("Arg: {} {} {}", orderNumber, location.getX(), location.getY());
        } else {
            logger.error("Agent " + getLocalName() + " - Incorrect number of arguments");
            doDelete();
        }
        // Read items to buy
        try (FileInputStream fin = new FileInputStream("order" + orderNumber + ".txt");
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
        while (PersonalAgentGuiActionsAdapter.isInstanceNull(orderNumber)) {}
        PersonalAgentGuiActionsAdapter.actionSetItems(orderNumber, items);
        // Contact shop
        addBehaviour(new ContactBehaviour(this, location));
    }

    public Codec getCodec() {
        return codec;
    }

    public Ontology getOntology() {
        return ontology;
    }

    public ObservableList<Item> getObservableListItems() {
        return items;
    }

    /**
     * Return the list of items but without max. price and status info.
     */
    public List<Item> getListItems() {
        List<Item> list = new ArrayList<>(items.size());
        for(Item i : items) {
            Item newItem = i.clone();
            newItem.setMaxPrice(0);
            newItem.setStatus("");
            list.add(newItem);
        }
        return list;
    }

    /**
     * Print a message in status box.
     */
    public void printStatus(String msg) {
        PersonalAgentGuiActionsAdapter.actionAppendStatusMsg(orderNumber, msg);
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void cancelOrder(String reason) {
        for (Item i : items) {
            i.setStatus("Cancelled.");
        }
        printStatus("The order was cancelled: " + reason);
    }

    public void updateItemStatus(String item, String status) {
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).getClass().getSimpleName().equalsIgnoreCase(item)) {
                Item it = items.get(i);
                it.setStatus(status);
                items.set(i,it);
                break;
            }
        }
    }
}