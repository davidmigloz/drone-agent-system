package com.davidflex.supermarket.ontologies.shop.concepts.wine;

import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;

@SuppressWarnings("unused")
public class Torres extends WineItem {

	public Torres() {
	}

	public Torres(int quantity, float price) {
		super(quantity, price);
	}

	public Torres(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return ShopOntologyVocabulary.TORRES;
	}
}