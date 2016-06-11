package com.davidflex.supermarket.ontologies.company.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;
import jade.content.Predicate;

import java.util.List;

/**
 * Request the warehouse to check its available stocks.
 *
 * @author  Constantin MASSON
 * @since   June 10, 2016
 */
@SuppressWarnings("unused")
public class CheckStockItemsRequest implements Predicate {
    private List<Item> items;

    public CheckStockItemsRequest(List<Item> listItems){
        this.items = listItems;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
