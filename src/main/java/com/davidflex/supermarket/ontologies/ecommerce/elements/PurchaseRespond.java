package com.davidflex.supermarket.ontologies.ecommerce.elements;

import java.util.List;

import jade.content.Predicate;

/**
 * The Personal Shopping Agent returns a list with the available products and
 * their prices.
 */
public class PurchaseRespond implements Predicate {
	private static final long serialVersionUID = 1L;

	public static final String NAME = "PurchaseRespond";

	public static final String ITEMS = "items";

	private List<Item> items;

	public List<Item> getItem() {
		return items;
	}

	public void setItem(List<Item> i) {
		items = i;
	}
}