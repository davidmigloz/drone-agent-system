package com.davidflex.supermarket.ontologies.shop.concepts.wine;

import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;

@SuppressWarnings("unused")
public class SentitsNegres extends WineItem {

	public SentitsNegres() {
	}

	public SentitsNegres(int quantity, float price) {
		super(quantity, price);
	}

	public SentitsNegres(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return ShopOntologyVocabulary.SENTITS;
	}
}