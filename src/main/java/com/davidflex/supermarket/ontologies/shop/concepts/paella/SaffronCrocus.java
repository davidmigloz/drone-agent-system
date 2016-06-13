package com.davidflex.supermarket.ontologies.shop.concepts.paella;

import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;

@SuppressWarnings("unused")
public class SaffronCrocus extends PaellaItem {

	public SaffronCrocus() {
	}

	public SaffronCrocus(int quantity, float price) {
		super(quantity, price);
	}

	public SaffronCrocus(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return ShopOntologyVocabulary.SAFFRON;
	}
}