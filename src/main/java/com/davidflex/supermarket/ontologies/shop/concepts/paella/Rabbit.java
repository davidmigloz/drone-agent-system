package com.davidflex.supermarket.ontologies.shop.concepts.paella;

import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;

@SuppressWarnings("unused")
public class Rabbit extends PaellaItem {

	public Rabbit() {
	}

	public Rabbit(int quantity, float price) {
		super(quantity, price);
	}

	public Rabbit(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return ShopOntologyVocabulary.RABBIT;
	}
}