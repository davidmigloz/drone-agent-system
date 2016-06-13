package com.davidflex.supermarket.ontologies.shop.concepts.paella;

import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;

@SuppressWarnings("unused")
public class OliveOil extends PaellaItem {

	public OliveOil() {
	}

	public OliveOil(int quantity, float price) {
		super(quantity, price);
	}

	public OliveOil(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return ShopOntologyVocabulary.OLIVEOIL;
	}
}