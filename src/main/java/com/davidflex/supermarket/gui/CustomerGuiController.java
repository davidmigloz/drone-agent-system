package com.davidflex.supermarket.gui;

import com.davidflex.supermarket.agents.customer.PersonalAgent;
import com.davidflex.supermarket.agents.utils.JadeUtils;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Item;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Customer gui controller.
 */
public class CustomerGuiController implements CustomerGuiActions {

    private static final Logger logger = LoggerFactory.getLogger(CustomerGuiController.class);

    private ContainerController container;
    private CustomerGui customerGui;
    private final AtomicInteger orderCounter = new AtomicInteger();

    @FXML
    private ComboBox<String> category;
    @FXML
    private ComboBox<String> product;
    @FXML
    private TextField quantity;
    @FXML
    private TextField maxPrice;
    @FXML
    private Button addButton;
    @FXML
    private TableView<Item> table;
    @FXML
    private TableColumn<Item, String> productCol;
    @FXML
    private TableColumn<Item, String> categoryCol;
    @FXML
    private TableColumn<Item, String> quantityCol;
    @FXML
    private TableColumn<Item, String> maxPriceCol;
    @FXML
    private TextField x;
    @FXML
    private TextField y;

    public CustomerGuiController() {
        // Create container for personalAgents
        container = JadeUtils.createContainer("personalAgents");
        // Adapter for CustomerGuiAgent to interact with GUI
        CustomerGuiActionsAdapter.setInstance(this);
    }

    @FXML
    private void initialize() {
        category.setOnAction(this::handleSelectCategory);
        addButton.setOnAction(this::handleAddButton);
        productCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));
        categoryCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        quantityCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuantity() + ""));
        maxPriceCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaxPrice() + "€"));
    }

    /**
     * Set data to GUI.
     */
    void setData(CustomerGui customerGui) {
        this.customerGui = customerGui;
        category.setItems(customerGui.getCategories());
        product.setItems(customerGui.getPaellaItems());
        table.setItems(customerGui.getItemsToBuy());
    }

    /**
     * Show the right products depending on the category chosen.
     */
    private void handleSelectCategory(ActionEvent event) {
        String selectedCategory = category.getSelectionModel().getSelectedItem();
        if(selectedCategory.equals("Paella")) {
            product.setItems(customerGui.getPaellaItems());
        } else if (selectedCategory.equals("Wine")){
            product.setItems(customerGui.getWineItems());
        }
    }

    /**
     * Add item to the itemsToBuy list.
     */
    private void handleAddButton(ActionEvent event) {
        String it = product.getSelectionModel().getSelectedItem();
        String cat = category.getSelectionModel().getSelectedItem();
        String q = quantity.getText();
        String mp = maxPrice.getText();

        if(cat != null && it != null && !q.equals("") && !mp.equals("")) {
            customerGui.addItemToBut(it, cat, Integer.parseInt(q), Integer.parseInt(mp));
        }
    }

    /**
     * Writes the order and creates a new personal agent to deal with it.
     */
    @FXML
    private void handleBuy() {
        if(customerGui.getItemsToBuy().size() == 0 || x.getText().equals("") || y.getText().equals("")) {
            return;
        }
        try {
            // Get number of order
            int num = orderCounter.getAndIncrement();
            logger.info("New order #" + num + " - " + customerGui.getItemsToBuy().size() + " items");
            // Write shopping list into a file
            try(FileOutputStream fout = new FileOutputStream("order" + Integer.toString(num) + ".txt");
                ObjectOutputStream oos = new ObjectOutputStream(fout)) {
                oos.writeObject(new ArrayList<>(customerGui.getItemsToBuy()));
            }
            // Clear items
            customerGui.getItemsToBuy().clear();
            // Create new personalAgent
            final AgentController entry =
                    container.createNewAgent("personalAgent#" + num,
                            PersonalAgent.class.getName(), new Object[]{num + "",
                            x.getText(), y.getText()});
            entry.start();
        } catch (IOException | StaleProxyException e) {
            logger.error("Error at handling new order", e);
        }
    }

    /**
     * Open the order modal window.
     */
    @Override
    public void createNewWindow(String windowTitle) {
        Platform.runLater(() -> customerGui.openNewOrderWindow(windowTitle));
    }
}
