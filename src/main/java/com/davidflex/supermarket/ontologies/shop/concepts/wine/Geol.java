package com.davidflex.supermarket.ontologies.shop.concepts.wine;

import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;

@SuppressWarnings("unused")
public class Geol extends WineItem {

	public Geol() {
	}

	public Geol(int quantity, float price) {
		super(quantity, price);
	}

	public Geol(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return ShopOntologyVocabulary.GEOL;
	}
}