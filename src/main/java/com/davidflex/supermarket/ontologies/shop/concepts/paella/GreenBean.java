package com.davidflex.supermarket.ontologies.shop.concepts.paella;

import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;

public class GreenBean extends PaellaItem {

	public GreenBean() {
	}

	public GreenBean(int quantity, float price) {
		super(quantity, price);
	}

	public GreenBean(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return ShopOntologyVocabulary.GREENBEA;
	}
}