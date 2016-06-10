package com.davidflex.supermarket.ontologies.company.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

import java.util.List;

/**
 * Created by moose on 2016-06-10.
 * Response from warehouse with list of its available stock.
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
