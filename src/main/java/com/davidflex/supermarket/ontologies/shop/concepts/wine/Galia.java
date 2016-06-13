package com.davidflex.supermarket.ontologies.shop.concepts.wine;

import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;

@SuppressWarnings("unused")
public class Galia extends WineItem {

	public Galia() {
	}

	public Galia(int quantity, float price) {
		super(quantity, price);
	}

	public Galia(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return ShopOntologyVocabulary.GALIA;
	}
}