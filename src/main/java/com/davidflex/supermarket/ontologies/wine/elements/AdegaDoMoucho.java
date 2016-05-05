package com.davidflex.supermarket.ontologies.wine.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

public class AdegaDoMoucho extends Item {
	private static final long serialVersionUID = 1L;

	public AdegaDoMoucho(int quantiy) {
		super(quantiy);
	}
	
	public AdegaDoMoucho(int quantiy, float price) {
		super(quantiy, price);
	}
}