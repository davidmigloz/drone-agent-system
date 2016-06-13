package com.davidflex.supermarket.ontologies.shop.concepts.paella;

import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;

@SuppressWarnings("unused")
public class LimaBeans extends PaellaItem  {

	public LimaBeans() {
	}

	public LimaBeans(int quantity, float price) {
		super(quantity, price);
	}

	public LimaBeans(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return ShopOntologyVocabulary.LIMABEAN;
	}
}