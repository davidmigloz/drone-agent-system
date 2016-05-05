package com.davidflex.supermarket.ontologies.paella.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

public class SaffronCrocus extends Item {
	private static final long serialVersionUID = 1L;

	public SaffronCrocus(int quantiy) {
		super(quantiy);
	}
	
	public SaffronCrocus(int quantiy, float price) {
		super(quantiy, price);
	}
}