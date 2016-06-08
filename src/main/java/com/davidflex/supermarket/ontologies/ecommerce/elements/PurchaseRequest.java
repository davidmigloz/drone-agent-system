package com.davidflex.supermarket.ontologies.ecommerce.elements;

import java.util.List;

import jade.content.Predicate;

/**
 * The Personal Agent ask for information about the items of the list (availability and prices).
 */
@SuppressWarnings("unused")
public class PurchaseRequest implements Predicate {
	
	private List<Item> items;

	public PurchaseRequest() {
	}

	public PurchaseRequest(List<Item> items) {
		this.items = items;
	}

	public List<Item> getItem() {
		return items;
	}

	public void setItem(List<Item> i) {
		items = i;
	}
}