package com.davidflex.supermarket.ontologies.shop.concepts.wine;

import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;

@SuppressWarnings("unused")
public class Gramona extends WineItem {

	public Gramona() {
	}

	public Gramona(int quantity, float price) {
		super(quantity, price);
	}

	public Gramona(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return ShopOntologyVocabulary.GRAMONA;
	}
}