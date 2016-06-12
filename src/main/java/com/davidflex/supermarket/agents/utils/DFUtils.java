package com.davidflex.supermarket.agents.utils;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

@SuppressWarnings("unused")
public class DFUtils {

    public static void registerInDF(Agent myAgent, String name, String type) throws FIPAException {
        ServiceDescription sd = new ServiceDescription();
        sd.setType(type);
        sd.setName(name);
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(myAgent.getAID());
        dfd.addServices(sd);
        DFService.register(myAgent, dfd);
    }

    public static DFAgentDescription[] searchInDFbyType(String type, Agent myAgent) throws FIPAException {
        ServiceDescription sd = new ServiceDescription();
        sd.setType(type);
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.addServices(sd);
        return DFService.search(myAgent, dfd);
    }

    public static DFAgentDescription[] searchInDFbyName(String name, Agent myAgent) throws FIPAException{
        ServiceDescription sd = new ServiceDescription();
        sd.setName(name);
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.addServices(sd);
        return DFService.search(myAgent, dfd);
    }

    public static boolean deregisterFromDF(Agent myAgent) throws FIPAException {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(myAgent.getAID());
        DFService.deregister(myAgent, dfd);
        ServiceDescription sd = new ServiceDescription();
        sd.setName(myAgent.getName());
        dfd.addServices(sd);
        return DFService.search(myAgent, dfd) == null;
    }
}