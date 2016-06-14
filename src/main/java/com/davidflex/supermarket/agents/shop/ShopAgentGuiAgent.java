package com.davidflex.supermarket.agents.shop;

import com.davidflex.supermarket.gui.ShopAgentGui;
import jade.core.Agent;
import javafx.application.Application;

/**
 * Agent for shopAgent GUI.
 */
@SuppressWarnings("unused")
    public class ShopAgentGuiAgent extends Agent {

    @Override
    protected void setup() {
        Application.launch(ShopAgentGui.class);
    }
}
