package com.davidflex.supermarket.ontologies.shop.concepts.wine;

import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;

@SuppressWarnings("unused")
public class AdegaDoMoucho extends WineItem {

	public AdegaDoMoucho() {
	}

	public AdegaDoMoucho(int quantity, float price) {
		super(quantity, price);
	}

	public AdegaDoMoucho(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return ShopOntologyVocabulary.ADEGA;
	}
}