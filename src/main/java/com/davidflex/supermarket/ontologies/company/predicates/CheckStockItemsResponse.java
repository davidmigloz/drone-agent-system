package com.davidflex.supermarket.ontologies.company.predicates;

import com.davidflex.supermarket.ontologies.ecommerce.concepts.Item;
import jade.content.Predicate;

import java.util.List;

/**
 * Response from warehouse with list of its available stock.
 *
 * @author  Constantin MASSON
 * @since   June 10, 2016
 */
@SuppressWarnings("unused")
public class CheckStockItemsResponse implements Predicate {
    private List<Item> items; //Items (With stock)

    public CheckStockItemsResponse() {
    }

    public CheckStockItemsResponse(List<Item> listItems) {
        this.items = listItems;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
