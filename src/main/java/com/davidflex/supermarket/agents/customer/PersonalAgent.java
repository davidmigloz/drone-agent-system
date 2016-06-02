package com.davidflex.supermarket.agents.customer;

import com.davidflex.supermarket.agents.gui.PersonalAgentGui;
import jade.core.Agent;
import javafx.application.Application;

public class PersonalAgent extends Agent {

    @Override
    protected void setup() {
        Application.launch(PersonalAgentGui.class);

    }
}