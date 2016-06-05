package com.davidflex.supermarket.agents.customer;

import com.davidflex.supermarket.agents.gui.PersonAgentGui;
import jade.core.Agent;
import javafx.application.Application;

public class PersonAgent extends Agent {
    @Override
    protected void setup() {
        Application.launch(PersonAgentGui.class);
    }
}
