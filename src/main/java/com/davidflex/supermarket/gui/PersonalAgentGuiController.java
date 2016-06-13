package com.davidflex.supermarket.gui;

import com.davidflex.supermarket.ontologies.ecommerce.concepts.Item;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

/**
 * Order modal window controller.
 */
public class PersonalAgentGuiController implements PersonalAgentGuiActions {

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
    private TableColumn<Item, String> price;
    @FXML
    private TableColumn<Item, String> status;
    @FXML
    private TextArea statusArea;

    public PersonalAgentGuiController() {
        PersonalAgentGuiActionsAdapter.setInstance(this);
    }

    @FXML
    private void initialize() {
        productCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));
        categoryCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        quantityCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuantity() + ""));
        maxPriceCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaxPrice() + "€"));
        price.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrice() + "€"));
        status.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getStatus() != null ? cellData.getValue().getStatus() : "?"));
        statusArea.appendText("Loading...");
    }

    @Override
    public void setItems(ObservableList<Item> items) {
        Platform.runLater(() -> table.setItems(items));
    }

    @Override
    public void appendStatusMsg(String msg) {
        Platform.runLater(() -> statusArea.appendText("\n" + msg));
    }
}
