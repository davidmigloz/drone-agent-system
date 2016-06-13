package com.davidflex.supermarket.ontologies.shop.concepts.wine;

import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;

@SuppressWarnings("unused")
public class Numanthia extends WineItem {

	public Numanthia() {
	}

	public Numanthia(int quantity, float price) {
		super(quantity, price);
	}

	public Numanthia(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return ShopOntologyVocabulary.NUMANTH;
	}
}