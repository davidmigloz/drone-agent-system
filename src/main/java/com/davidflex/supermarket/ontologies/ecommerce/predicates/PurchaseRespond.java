package com.davidflex.supermarket.ontologies.ecommerce.predicates;

import java.util.List;

import com.davidflex.supermarket.ontologies.ecommerce.concepts.Item;
import jade.content.Predicate;

/**
 * The PersonalShopAgent returns a list with the available products and
 * their prices.
 */
@SuppressWarnings("unused")
public class PurchaseRespond implements Predicate {

	private long orderID;
	private List<Item> items;

	public PurchaseRespond() {
	}

	public PurchaseRespond(long orderID, List<Item> items) {
		this.orderID = orderID;
		this.items = items;
	}

	public List<Item> getItem() {
		return items;
	}

	public void setItem(List<Item> i) {
		items = i;
	}

	public long getOrderID() {
		return orderID;
	}

	public void setOrderID(long orderID) {
		this.orderID = orderID;
	}
}