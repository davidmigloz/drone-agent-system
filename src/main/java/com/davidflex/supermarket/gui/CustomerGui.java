package com.davidflex.supermarket.gui;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;
import com.davidflex.supermarket.ontologies.shop.paella.*;
import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;
import com.davidflex.supermarket.ontologies.shop.wine.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main application.
 */
public class CustomerGui extends Application implements ShopOntologyVocabulary {

    private final ObservableList<String> categories = FXCollections.observableArrayList(PAELLA, WINE);

    private final ObservableList<String> paellaItems = FXCollections.observableArrayList(
                    CHICKEN, GREENBEA, LIMABEAN, OLIVEOIL, RABBIT, RICE, SAFFRON, SALT, TOMATO);

    private final ObservableList<String> wineItems = FXCollections.observableArrayList(
            ADEGA, FERRER, GALIA, GEOL, GRAMONA, NUMANTH, PLAZUEL, SENTITS, TEIXAR, TORRES);

    private ObservableList<Item> itemsToBuy = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/gui.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Personal Agent");
        primaryStage.setResizable(false);

        final Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();

        CustomerGuiController controller = loader.getController();
        controller.setData(this);

        primaryStage.show();
    }

    @SuppressWarnings("ConstantConditions")
    void openNewOrderWindow(String windowTitle) {
        // Load the fxml file and create a new stage for the popup dialog
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/personalAgentGui.fxml"));
        AnchorPane page = null;
        try {
            page = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Create the dialog Stage
        Stage dialogStage = new Stage();
        dialogStage.setTitle(windowTitle);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        // Show the dialog
        dialogStage.show();
    }

    ObservableList<String> getCategories() {
        return categories;
    }

    ObservableList<String> getPaellaItems() {
        return paellaItems;
    }

    ObservableList<String> getWineItems() {
        return wineItems;
    }

    ObservableList<Item> getItemsToBuy() {
        return itemsToBuy;
    }

    /**
     * Add an item to the itemsToBuy list.
     */
    void addItemToBut(String item, String category, int quantity, long maxPrice) {
        Item newItem = null;
        if(category.equals("Paella")) {
            switch (item) {
                case CHICKEN:
                    newItem = new Chicken(maxPrice, quantity);
                    break;
                case GREENBEA:
                    newItem = new GreenBean(maxPrice, quantity);
                    break;
                case LIMABEAN:
                    newItem = new LimaBeans(maxPrice, quantity);
                    break;
                case OLIVEOIL:
                    newItem = new OliveOil(maxPrice, quantity);
                    break;
                case RABBIT:
                    newItem = new Rabbit(maxPrice, quantity);
                    break;
                case RICE:
                    newItem = new Rabbit(maxPrice, quantity);
                    break;
                case SAFFRON:
                    newItem = new SaffronCrocus(maxPrice, quantity);
                    break;
                case SALT:
                    newItem = new Salt(maxPrice, quantity);
                    break;
                case TOMATO:
                    newItem = new Tomato(maxPrice, quantity);
                    break;
            }
        } else if(category.equals("Wine")){
            switch (item) {
                case ADEGA:
                    newItem = new AdegaDoMoucho(maxPrice, quantity);
                    break;
                case FERRER:
                    newItem = new FerrerBobet(maxPrice, quantity);
                    break;
                case GALIA:
                    newItem = new Galia(maxPrice, quantity);
                    break;
                case GEOL:
                    newItem = new Geol(maxPrice, quantity);
                    break;
                case GRAMONA:
                    newItem = new Gramona(maxPrice, quantity);
                    break;
                case NUMANTH:
                    newItem = new Numanthia(maxPrice, quantity);
                    break;
                case PLAZUEL:
                    newItem = new Plazuela(maxPrice, quantity);
                    break;
                case SENTITS:
                    newItem = new SentitsNegres(maxPrice, quantity);
                    break;
                case TEIXAR:
                    newItem = new Teixar(maxPrice, quantity);
                    break;
                case TORRES:
                    newItem = new Torres(maxPrice, quantity);
                    break;
            }
        }
        itemsToBuy.add(newItem);
    }
}
