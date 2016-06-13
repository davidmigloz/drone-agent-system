package com.davidflex.supermarket.ontologies.ecommerce.concepts;

import jade.content.Concept;

/**
 * Item from the transaction.
 */
public abstract class Item implements Concept, Cloneable {

	private int quantity;
	private float price;
	private float maxPrice;
	private String status;

	public Item() {
	}

	public Item(int quantity, float price) {
		setQuantity(quantity);
		setPrice(price);
	}

	public Item(float maxPrice, int quantity) {
		this.maxPrice = maxPrice;
		this.quantity = quantity;
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

	public float getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(float maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public Item clone() {
		try {
			return (Item) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}