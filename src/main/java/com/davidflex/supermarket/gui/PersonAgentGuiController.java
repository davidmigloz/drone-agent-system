package com.davidflex.supermarket.gui;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PersonAgentGuiController {

    private PersonAgentGui personAgentGui;

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
    private void initialize() {
        category.setOnAction(this::handleSelectCategory);
        addButton.setOnAction(this::handleAddButton);
        productCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));
        categoryCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        quantityCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuantity() + ""));
        maxPriceCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrice() + "€"));
    }

    void setData(PersonAgentGui personAgentGui) {
        this.personAgentGui = personAgentGui;
        category.setItems(personAgentGui.getCategories());
        product.setItems(personAgentGui.getPaellaItems());
        table.setItems(personAgentGui.getItemsToBuy());
    }

    private void handleSelectCategory(ActionEvent event) {
        String selectedCategory = category.getSelectionModel().getSelectedItem();
        if(selectedCategory.equals("Paella")) {
            product.setItems(personAgentGui.getPaellaItems());
        } else if (selectedCategory.equals("Wine")){
            product.setItems(personAgentGui.getWineItems());
        }
    }

    private void handleAddButton(ActionEvent event) {
        personAgentGui.addItemToBut(product.getSelectionModel().getSelectedItem(),
                category.getSelectionModel().getSelectedItem(),
                Integer.parseInt(quantity.getText()), Integer.parseInt(maxPrice.getText()));
    }
}
