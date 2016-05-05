package com.davidflex.supermarket.ontologies.wine.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

public class Numanthia extends Item {
	private static final long serialVersionUID = 1L;

	public Numanthia(int quantiy) {
		super(quantiy);
	}
	
	public Numanthia(int quantiy, float price) {
		super(quantiy, price);
	}
}