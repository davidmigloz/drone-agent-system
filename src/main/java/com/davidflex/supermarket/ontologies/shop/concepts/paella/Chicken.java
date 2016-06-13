package com.davidflex.supermarket.ontologies.shop.concepts.paella;

import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;

public class Chicken extends PaellaItem {

	public Chicken() {
	}

	public Chicken(int quantity, float price) {
		super(quantity, price);
	}

	public Chicken(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return ShopOntologyVocabulary.CHICKEN;
	}
}