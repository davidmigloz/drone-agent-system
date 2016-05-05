package com.davidflex.supermarket.ontologies.wine.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

public class Torres extends Item {
	private static final long serialVersionUID = 1L;

	public Torres(int quantiy) {
		super(quantiy);
	}
	
	public Torres(int quantiy, float price) {
		super(quantiy, price);
	}
}