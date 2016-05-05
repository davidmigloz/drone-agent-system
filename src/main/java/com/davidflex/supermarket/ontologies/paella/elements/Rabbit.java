package com.davidflex.supermarket.ontologies.paella.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

public class Rabbit extends Item {
	private static final long serialVersionUID = 1L;

	public Rabbit(int quantiy) {
		super(quantiy);
	}
	
	public Rabbit(int quantiy, float price) {
		super(quantiy, price);
	}
}