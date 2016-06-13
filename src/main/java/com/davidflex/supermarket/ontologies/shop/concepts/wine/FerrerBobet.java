package com.davidflex.supermarket.ontologies.shop.concepts.wine;

import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;

@SuppressWarnings("unused")
public class FerrerBobet extends WineItem {

	public FerrerBobet() {
	}

	public FerrerBobet(int quantity, float price) {
		super(quantity, price);
	}

	public FerrerBobet(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return ShopOntologyVocabulary.FERRER;
	}
}