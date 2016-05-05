package com.davidflex.supermarket.ontologies.wine.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

public class Plazuela extends Item {
	private static final long serialVersionUID = 1L;

	public Plazuela(int quantiy) {
		super(quantiy);
	}
	
	public Plazuela(int quantiy, float price) {
		super(quantiy, price);
	}
}