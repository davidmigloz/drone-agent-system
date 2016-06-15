package com.davidflex.supermarket.gui;

import com.davidflex.supermarket.agents.shop.ShopAgent;
import com.davidflex.supermarket.agents.utils.JadeUtils;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Location;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Controller for shopAgent GUI.
 */
public class ShopAgentGuiController implements ShopAgentGuiActions {

    private static final Logger logger = LoggerFactory.getLogger(ShopAgentGuiController.class);

    @FXML
    private Canvas canvas;
    @FXML
    private TextField warehouses;
    @FXML
    private TextField drones;
    @FXML
    private TextField customers;

    private Image bg;
    private Image warehouseImg;
    private Image droneImg;
    private Image customerImg;

    public ShopAgentGuiController() {
        // Adapter for ShopAgent to interact with GUI
        ShopAgentGuiActionsAdapter.setInstance(this);
        // Get images
        bg = new Image(getClass().getResourceAsStream("/img/bg.png"));
        warehouseImg = new Image(getClass().getResourceAsStream("/img/warehouse.png"));
        droneImg = new Image(getClass().getResourceAsStream("/img/drone.png"));
        customerImg = new Image(getClass().getResourceAsStream("/img/customer.png"));
    }

    @FXML
    private void initialize() {
        // Clear canvas
        clearCanvas();
        // Lunch ShopAgent
        try {
            final AgentController entry = JadeUtils.createContainer("shop").createNewAgent("shop",
                    ShopAgent.class.getName(), new Object[]{});
            entry.start();
        } catch (StaleProxyException e) {
            logger.error("Error launching ShopAgent", e);
        }
    }

    @Override
    public void updateInfo(List<Location> warehouses, List<Location> drones, List<Location> customers) {
        clearCanvas();
        // Warehouses
        this.warehouses.setText(warehouses.size() + "");
        for(Location w : warehouses) {
            drawImage(w.getX(), w.getY(), warehouseImg);
        }
        // Customers
        this.customers.setText(customers.size() + "");
        for(Location c : customers) {
            drawImage(c.getX(), c.getY(), customerImg);
        }
        // Drones
        this.drones.setText(drones.size() + "");
        for(Location d : drones) {
            drawImage(d.getX(), d.getY(), droneImg);
        }
    }

    private void clearCanvas() {
        canvas.getGraphicsContext2D().setFill(new ImagePattern(bg, 0, 0, bg.getWidth(), bg.getHeight(), false));
        canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawImage(int x, int y, Image img) {
        canvas.getGraphicsContext2D().drawImage(img, x - img.getWidth()/2, y - img.getHeight()/2);
    }
}
