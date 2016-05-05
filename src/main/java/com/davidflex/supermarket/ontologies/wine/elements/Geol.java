package com.davidflex.supermarket.ontologies.wine.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

public class Geol extends Item {
	private static final long serialVersionUID = 1L;

	public Geol(int quantiy) {
		super(quantiy);
	}
	
	public Geol(int quantiy, float price) {
		super(quantiy, price);
	}
}