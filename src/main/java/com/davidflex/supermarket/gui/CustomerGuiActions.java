package com.davidflex.supermarket.gui;

/**
 * Interactions CustomerGuiAgent <-> GUI.
 */
interface CustomerGuiActions {

    /**
     * Open a modal window with info about the order.
     *
     * @param windowTitle title of the modal
     */
    void createNewWindow(String windowTitle);

}
