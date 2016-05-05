package com.davidflex.supermarket.ontologies.wine.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

public class Teixar extends Item {
	private static final long serialVersionUID = 1L;

	public Teixar(int quantiy) {
		super(quantiy);
	}
	
	public Teixar(int quantiy, float price) {
		super(quantiy, price);
	}
}