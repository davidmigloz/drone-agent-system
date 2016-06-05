package com.davidflex.supermarket.ontologies.wine.elements;

import com.davidflex.supermarket.ontologies.wine.WineOntologyVocabulary;

public class Torres extends WineItem {
	private static final long serialVersionUID = 1L;
	
	public Torres(int quantity, float price) {
		super(quantity, price);
	}

	public Torres(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return WineOntologyVocabulary.TORRES;
	}
}