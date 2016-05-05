package com.davidflex.supermarket.ontologies.paella.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

public class Tomato extends Item {
	private static final long serialVersionUID = 1L;

	public Tomato(int quantiy) {
		super(quantiy);
	}
	
	public Tomato(int quantiy, float price) {
		super(quantiy, price);
	}
}