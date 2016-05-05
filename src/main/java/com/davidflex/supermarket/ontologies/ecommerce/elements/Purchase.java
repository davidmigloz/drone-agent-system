package com.davidflex.supermarket.ontologies.ecommerce.elements;

import java.util.List;

import jade.content.AgentAction;
import jade.core.AID;

/**
 * Purchase a list of products.
 */
public class Purchase implements AgentAction {
	private static final long serialVersionUID = 1L;

	private AID buyer;
	private List<Item> items;

	public Purchase() {
	}

	public Purchase(AID buyer, List<Item> item) {
		setBuyer(buyer);
		setItem(item);
	}

	public AID getBuyer() {
		return buyer;
	}

	public void setBuyer(AID id) {
		buyer = id;
	}

	public List<Item> getItem() {
		return items;
	}

	public void setItem(List<Item> i) {
		items = i;
	}
}