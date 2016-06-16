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
 * By default, drones are in the warehouse and available.
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
        //Create the drone's arguments
        Object[] args = new Object[4];
        args[0] = ((WarehouseAgent)getAgent()).getShopAgent().getLocalName();
        args[1] = getAgent().getAID().getLocalName();
        args[2] = ((WarehouseAgent)getAgent()).getLocation().getX()+""; // +"" important
        args[3] = ((WarehouseAgent)getAgent()).getLocation().getY()+"";

        //Create all drone agent.
        for(int k=0; k<((WarehouseAgent)getAgent()).getFleetSize(); k++){
            String droneName = getAgent().getLocalName() + "-drone-"+k;
            try{
                //Try to create a drone agent.
                getAgent().getContainerController().createNewAgent(
                        droneName,
                        DroneAgent.class.getName(),
                        args
                ).start();
                AID newDroneAID = new AID(droneName, AID.ISLOCALNAME);
                //Add the drone in warehouse list.
                ((WarehouseAgent)getAgent()).getFleet().put(newDroneAID, true);
            }
            catch (StaleProxyException e) {e.printStackTrace();
                logger.error("Unable to create the drone "+droneName);
            }
        }
    }
}
