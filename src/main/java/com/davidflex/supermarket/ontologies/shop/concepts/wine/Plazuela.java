package com.davidflex.supermarket.ontologies.shop.concepts.wine;

import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;

@SuppressWarnings("unused")
public class Plazuela extends WineItem {

	public Plazuela() {
	}

	public Plazuela(int quantity, float price) {
		super(quantity, price);
	}

	public Plazuela(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return ShopOntologyVocabulary.PLAZUEL;
	}
}