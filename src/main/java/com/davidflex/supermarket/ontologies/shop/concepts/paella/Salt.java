package com.davidflex.supermarket.ontologies.shop.concepts.paella;

import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;

@SuppressWarnings("unused")
public class Salt extends PaellaItem {

	public Salt() {
	}

	public Salt(int quantity, float price) {
		super(quantity, price);
	}

	public Salt(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return ShopOntologyVocabulary.SALT;
	}
}