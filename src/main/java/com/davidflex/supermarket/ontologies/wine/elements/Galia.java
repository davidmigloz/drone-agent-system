package com.davidflex.supermarket.ontologies.wine.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

public class Galia extends Item {
	private static final long serialVersionUID = 1L;

	public Galia(int quantiy) {
		super(quantiy);
	}
	
	public Galia(int quantiy, float price) {
		super(quantiy, price);
	}
}