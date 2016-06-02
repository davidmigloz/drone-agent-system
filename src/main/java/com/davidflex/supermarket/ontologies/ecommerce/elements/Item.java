package com.davidflex.supermarket.ontologies.ecommerce.elements;

import jade.content.Concept;

public abstract class Item implements Concept {
	private static final long serialVersionUID = 1L;

	private int quantity;
	private float price;

	public Item(int quantity, float price) {
		setQuantity(quantity);
		setPrice(price);
	}

	public abstract String getCategory();

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
}