package com.davidflex.supermarket.ontologies.paella.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

public class Rice extends Item{
	private static final long serialVersionUID = 1L;

	public Rice(int quantiy) {
		super(quantiy);
	}
	
	public Rice(int quantiy, float price) {
		super(quantiy, price);
	}
}