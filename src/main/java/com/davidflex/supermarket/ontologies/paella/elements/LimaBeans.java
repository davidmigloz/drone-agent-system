package com.davidflex.supermarket.ontologies.paella.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

public class LimaBeans extends Item {
	private static final long serialVersionUID = 1L;

	public LimaBeans(int quantiy) {
		super(quantiy);
	}
	
	public LimaBeans(int quantiy, float price) {
		super(quantiy, price);
	}
}