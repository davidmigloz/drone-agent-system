package com.davidflex.supermarket.ontologies.wine.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

public class SentitsNegres extends Item {
	private static final long serialVersionUID = 1L;

	public SentitsNegres(int quantiy) {
		super(quantiy);
	}
	
	public SentitsNegres(int quantiy, float price) {
		super(quantiy, price);
	}
}