package com.davidflex.supermarket.agents.gui;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;
import com.davidflex.supermarket.ontologies.paella.PaellaOntologyVocabulary;
import com.davidflex.supermarket.ontologies.paella.elements.*;
import com.davidflex.supermarket.ontologies.wine.WineOntologyVocabulary;
import com.davidflex.supermarket.ontologies.wine.elements.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PersonalAgentGui extends Application implements PaellaOntologyVocabulary, WineOntologyVocabulary{

    private final ObservableList<String> categories = FXCollections.observableArrayList(PAELLA, WINE);

    private final ObservableList<String> paellaItems = FXCollections.observableArrayList(
                    CHICKEN, GREENBEA, LIMABEAN, OLIVEOIL, RABBIT, RICE, SAFFRON, SALT, TOMATO);

    private final ObservableList<String> wineItems = FXCollections.observableArrayList(
            ADEGA, FERRER, GALIA, GEOL, GRAMONA, NUMANTH, PLAZUEL, SENTITS, TEIXAR, TORRES);

    private ObservableList<Item> itemsToBuy = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/gui.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Personal Agent");
        primaryStage.setResizable(false);

        final Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();

        PersonalAgentGuiController controller = loader.getController();
        controller.setData(this);

        primaryStage.show();
    }

    public ObservableList<String> getCategories() {
        return categories;
    }

    public ObservableList<String> getPaellaItems() {
        return paellaItems;
    }

    public ObservableList<String> getWineItems() {
        return wineItems;
    }

    public ObservableList<Item> getItemsToBuy() {
        return itemsToBuy;
    }

    public void addItemToBut(String item, String category, int quantity, int maxPrice) {
        Item newItem = null;

        if(category.equals("Paella")) {
            switch (item) {
                case CHICKEN:
                    newItem = new Chicken(quantity, maxPrice);
                    break;
                case GREENBEA:
                    newItem = new GreenBean(quantity, maxPrice);
                    break;
                case LIMABEAN:
                    newItem = new LimaBeans(quantity, maxPrice);
                    break;
                case OLIVEOIL:
                    newItem = new OliveOil(quantity, maxPrice);
                    break;
                case RABBIT:
                    newItem = new Rabbit(quantity, maxPrice);
                    break;
                case RICE:
                    newItem = new Rabbit(quantity, maxPrice);
                    break;
                case SAFFRON:
                    newItem = new SaffronCrocus(quantity, maxPrice);
                    break;
                case SALT:
                    newItem = new Salt(quantity, maxPrice);
                    break;
                case TOMATO:
                    newItem = new Tomato(quantity, maxPrice);
                    break;
            }
        } else if(category.equals("Wine")){
            switch (item) {
                case ADEGA:
                    newItem = new AdegaDoMoucho(quantity, maxPrice);
                    break;
                case FERRER:
                    newItem = new FerrerBobet(quantity, maxPrice);
                    break;
                case GALIA:
                    newItem = new Galia(quantity, maxPrice);
                    break;
                case GEOL:
                    newItem = new Geol(quantity, maxPrice);
                    break;
                case GRAMONA:
                    newItem = new Gramona(quantity, maxPrice);
                    break;
                case NUMANTH:
                    newItem = new Numanthia(quantity, maxPrice);
                    break;
                case PLAZUEL:
                    newItem = new Plazuela(quantity, maxPrice);
                    break;
                case SENTITS:
                    newItem = new SentitsNegres(quantity, maxPrice);
                    break;
                case TEIXAR:
                    newItem = new Teixar(quantity, maxPrice);
                    break;
                case TORRES:
                    newItem = new Torres(quantity, maxPrice);
                    break;
            }
        }
        itemsToBuy.add(newItem);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
