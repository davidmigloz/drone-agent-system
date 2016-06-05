package com.davidflex.supermarket.agents.customer;


import com.davidflex.supermarket.gui.CustomerGui;
import jade.core.Agent;
import javafx.application.Application;

@SuppressWarnings("unused")
public class CustomerGuiAgent extends Agent {
    @Override
    protected void setup() {
        Application.launch(CustomerGui.class);
    }
}
