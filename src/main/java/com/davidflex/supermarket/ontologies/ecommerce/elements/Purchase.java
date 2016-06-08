package com.davidflex.supermarket.ontologies.ecommerce.elements;

import java.util.List;

import jade.content.AgentAction;

/**
 * Purchase the final list of items.
 */
@SuppressWarnings("unused")
public class Purchase implements AgentAction {

	private List<Item> items;

	public Purchase() {
	}

	public Purchase(List<Item> items) {
		this.items = items;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
}