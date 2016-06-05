package com.davidflex.supermarket.gui;

/**
 * Interactions CustomerGuiAgent <-> GUI.
 */
public class CustomerGuiActionsAdapter {

    private static CustomerGuiActions instance;

    public CustomerGuiActionsAdapter() {
        throw new IllegalStateException("Not instantiable.");
    }

    static void setInstance(CustomerGuiActions instance) {
        CustomerGuiActionsAdapter.instance = instance;
    }

    public static void actionCreateNewWindow(String windowTitle) {
        instance.createNewWindow(windowTitle);
    }
}
