package com.davidflex.supermarket.ontologies.paella.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

public class GreenBean extends Item {
	private static final long serialVersionUID = 1L;

	public GreenBean(int quantiy) {
		super(quantiy);
	}

	public GreenBean(int quantiy, float price) {
		super(quantiy, price);
	}
}