package com.davidflex.supermarket.agents.utils;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;

public class JadeUtils {

    public static AgentContainer createContainer(String containerName) {
        Runtime rt = jade.core.Runtime.instance();
        ProfileImpl profileImpl = new ProfileImpl(false);
        profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
        profileImpl.setParameter(ProfileImpl.CONTAINER_NAME, containerName);
        return rt.createAgentContainer(profileImpl);
    }
}
