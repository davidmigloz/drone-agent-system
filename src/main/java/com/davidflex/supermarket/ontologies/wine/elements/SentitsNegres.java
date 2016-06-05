package com.davidflex.supermarket.ontologies.wine.elements;

import com.davidflex.supermarket.ontologies.wine.WineOntologyVocabulary;

public class SentitsNegres extends WineItem {
	private static final long serialVersionUID = 1L;
	
	public SentitsNegres(int quantity, float price) {
		super(quantity, price);
	}

	public SentitsNegres(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return WineOntologyVocabulary.SENTITS;
	}
}