package com.davidflex.supermarket.ontologies.paella.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

public class OliveOil extends Item {
	private static final long serialVersionUID = 1L;

	public OliveOil(int quantiy) {
		super(quantiy);
	}
	
	public OliveOil(int quantiy, float price) {
		super(quantiy, price);
	}
}