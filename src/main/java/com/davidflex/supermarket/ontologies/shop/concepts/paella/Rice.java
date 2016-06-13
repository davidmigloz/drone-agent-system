package com.davidflex.supermarket.ontologies.shop.concepts.paella;

import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;

@SuppressWarnings("unused")
public class Rice extends PaellaItem{

	public Rice() {
	}

	public Rice(int quantity, float price) {
		super(quantity, price);
	}

	public Rice(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return ShopOntologyVocabulary.RICE;
	}
}