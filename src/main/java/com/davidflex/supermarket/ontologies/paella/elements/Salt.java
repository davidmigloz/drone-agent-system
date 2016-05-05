package com.davidflex.supermarket.ontologies.paella.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

public class Salt extends Item {
	private static final long serialVersionUID = 1L;

	public Salt(int quantiy) {
		super(quantiy);
	}
	
	public Salt(int quantiy, float price) {
		super(quantiy, price);
	}
}