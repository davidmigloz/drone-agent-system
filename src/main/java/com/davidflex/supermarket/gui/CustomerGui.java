package com.davidflex.supermarket.gui;

import com.davidflex.supermarket.ontologies.ecommerce.concepts.Item;
import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;
import com.davidflex.supermarket.ontologies.shop.concepts.paella.*;
import com.davidflex.supermarket.ontologies.shop.concepts.wine.*;
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
import java.util.ArrayList;
import java.util.List;

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

        primaryStage.setTitle("Customer GUI");
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
        if (category.equals("Paella")) {
            switch (item) {
                case CHICKEN:
                    if (!increaseQuantity(Chicken.class.getSimpleName(), maxPrice, quantity)) {
                        itemsToBuy.add(new Chicken(maxPrice, quantity));
                    }
                    break;
                case GREENBEA:
                    if (!increaseQuantity(GreenBean.class.getSimpleName(), maxPrice, quantity)) {
                        itemsToBuy.add(new GreenBean(maxPrice, quantity));
                    }
                    break;
                case LIMABEAN:
                    if (!increaseQuantity(LimaBeans.class.getSimpleName(), maxPrice, quantity)) {
                        itemsToBuy.add(new LimaBeans(maxPrice, quantity));
                    }
                    break;
                case OLIVEOIL:
                    if (!increaseQuantity(OliveOil.class.getSimpleName(), maxPrice, quantity)) {
                        itemsToBuy.add(new OliveOil(maxPrice, quantity));
                    }
                    break;
                case RABBIT:
                    if (!increaseQuantity(Rabbit.class.getSimpleName(), maxPrice, quantity)) {
                        itemsToBuy.add(new Rabbit(maxPrice, quantity));
                    }
                    break;
                case RICE:
                    if (!increaseQuantity(Rice.class.getSimpleName(), maxPrice, quantity)) {
                        itemsToBuy.add(new Rice(maxPrice, quantity));
                    }
                    break;
                case SAFFRON:
                    if (!increaseQuantity(SaffronCrocus.class.getSimpleName(), maxPrice, quantity)) {
                        itemsToBuy.add(new SaffronCrocus(maxPrice, quantity));
                    }
                    break;
                case SALT:
                    if (!increaseQuantity(Salt.class.getSimpleName(), maxPrice, quantity)) {
                        itemsToBuy.add(new Salt(maxPrice, quantity));
                    }
                    break;
                case TOMATO:
                    if (!increaseQuantity(Tomato.class.getSimpleName(), maxPrice, quantity)) {
                        itemsToBuy.add(new Tomato(maxPrice, quantity));
                    }
                    break;
            }
        } else if (category.equals("Wine")) {
            switch (item) {
                case ADEGA:
                    if (!increaseQuantity(AdegaDoMoucho.class.getSimpleName(), maxPrice, quantity)) {
                        itemsToBuy.add(new AdegaDoMoucho(maxPrice, quantity));
                    }
                    break;
                case FERRER:
                    if (!increaseQuantity(FerrerBobet.class.getSimpleName(), maxPrice, quantity)) {
                        itemsToBuy.add(new FerrerBobet(maxPrice, quantity));
                    }
                    break;
                case GALIA:
                    if (!increaseQuantity(Galia.class.getSimpleName(), maxPrice, quantity)) {
                        itemsToBuy.add(new Galia(maxPrice, quantity));
                    }
                    break;
                case GEOL:
                    if (!increaseQuantity(Geol.class.getSimpleName(), maxPrice, quantity)) {
                        itemsToBuy.add(new Geol(maxPrice, quantity));
                    }
                    break;
                case GRAMONA:
                    if (!increaseQuantity(Gramona.class.getSimpleName(), maxPrice, quantity)) {
                        itemsToBuy.add(new Gramona(maxPrice, quantity));
                    }
                    break;
                case NUMANTH:
                    if (!increaseQuantity(Numanthia.class.getSimpleName(), maxPrice, quantity)) {
                        itemsToBuy.add(new Numanthia(maxPrice, quantity));
                    }
                    break;
                case PLAZUEL:
                    if (!increaseQuantity(Plazuela.class.getSimpleName(), maxPrice, quantity)) {
                        itemsToBuy.add(new Plazuela(maxPrice, quantity));
                    }
                    break;
                case SENTITS:
                    if (!increaseQuantity(SentitsNegres.class.getSimpleName(), maxPrice, quantity)) {
                        itemsToBuy.add(new SentitsNegres(maxPrice, quantity));
                    }
                    break;
                case TEIXAR:
                    if (!increaseQuantity(Teixar.class.getSimpleName(), maxPrice, quantity)) {
                        itemsToBuy.add(new Teixar(maxPrice, quantity));
                    }
                    break;
                case TORRES:
                    if (!increaseQuantity(Torres.class.getSimpleName(), maxPrice, quantity)) {
                        itemsToBuy.add(new Torres(maxPrice, quantity));
                    }
                    break;
            }
        }
    }

    /**
     * Checks if an item is already in the list. If it is, it updates its quantity
     * and take the bigest maxPrice.
     *
     * @return true if the item was already in the list
     */
    private boolean increaseQuantity(String item, long maxPrice, int quantity) {
        for (Item i : itemsToBuy) {
            if (i.getClass().getSimpleName().equals(item)) {
                i.setQuantity(i.getQuantity() + quantity);
                if (i.getMaxPrice() < maxPrice) {
                    i.setMaxPrice(maxPrice);
                }
                refresh();
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("CollectionAddedToSelf")
    private void refresh() {
        List<Item> l = new ArrayList<>(itemsToBuy);
        itemsToBuy.removeAll(itemsToBuy);
        itemsToBuy.addAll(l);
    }
}
