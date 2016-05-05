package com.davidflex.supermarket.ontologies.paella.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

public class Chicken extends Item {
	private static final long serialVersionUID = 1L;

	public Chicken(int quantiy) {
		super(quantiy);
	}
	
	public Chicken(int quantiy, float price) {
		super(quantiy, price);
	}
}