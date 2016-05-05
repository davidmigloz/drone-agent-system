package com.davidflex.supermarket.ontologies.wine.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

public class Gramona extends Item {
	private static final long serialVersionUID = 1L;

	public Gramona(int quantiy) {
		super(quantiy);
	}
	
	public Gramona(int quantiy, float price) {
		super(quantiy, price);
	}
}