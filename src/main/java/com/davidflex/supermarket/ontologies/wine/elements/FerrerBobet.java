package com.davidflex.supermarket.ontologies.wine.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

public class FerrerBobet extends Item {
	private static final long serialVersionUID = 1L;

	public FerrerBobet(int quantiy) {
		super(quantiy);
	}
	
	public FerrerBobet(int quantiy, float price) {
		super(quantiy, price);
	}
}