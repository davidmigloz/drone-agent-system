package com.davidflex.supermarket.ontologies.ecommerce.elements;

import jade.content.Concept;

public class Item implements Concept {
	private static final long serialVersionUID = 1L;

	private int quantity;
	private float price;

	public Item() {
	}

	public Item(int quantiy) {
		setQuantity(quantity);
	}

	public Item(int quantiy, float price) {
		setQuantity(quantity);
		setPrice(price);
	}

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