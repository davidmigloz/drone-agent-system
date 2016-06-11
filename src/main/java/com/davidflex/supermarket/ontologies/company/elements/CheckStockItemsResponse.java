package com.davidflex.supermarket.ontologies.company.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

import java.util.List;

/**
 * Response from warehouse with list of its available stock.
 *
 * @author  Constantin MASSON
 * @since   June 10, 2016
 */
@SuppressWarnings("unused")
public class CheckStockItemsResponse {
    private List<Item> items;

    public CheckStockItemsResponse(List<Item> listItems){
        this.items = listItems;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
