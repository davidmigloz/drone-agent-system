package com.davidflex.supermarket.agents.behaviours.warehouse_agent;

import com.davidflex.supermarket.agents.shop.DroneAgent;
import com.davidflex.supermarket.agents.shop.WarehouseAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.wrapper.StaleProxyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create a fleet of drone and link it with the Warehouse agent.
 *
 * @since   June 12, 2016
 * @author  Constantin MASSON
 */
public class SetupFleetBehavior extends OneShotBehaviour{
    private static final Logger logger = LoggerFactory.getLogger(SetupFleetBehavior.class);

    public SetupFleetBehavior(Agent agent){
        super(agent);
    }

    @Override
    public void action() {
        //Create all drone agent.
        for(int k=0; k<((WarehouseAgent)getAgent()).getFleetSize(); k++){
            String droneName = "drone-"+k;
            try{
                //Try to create a drone agent.
                ((WarehouseAgent) getAgent()).getContainer().createNewAgent(
                        droneName,
                        DroneAgent.class.getName(),
                        new Object[]{}
                ).start();
                AID newDroneAID = new AID(droneName, AID.ISLOCALNAME);
                //Add the drone in warehouse list.
                ((WarehouseAgent)getAgent()).getFleet().add(newDroneAID);
            }
            catch (StaleProxyException e) {e.printStackTrace();
                logger.error("Unable to create the drone "+droneName);
            }
        }
    }
}
