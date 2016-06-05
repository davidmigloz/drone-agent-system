package com.davidflex.supermarket.ontologies.wine.elements;

import com.davidflex.supermarket.ontologies.wine.WineOntologyVocabulary;

public class Numanthia extends WineItem {
	private static final long serialVersionUID = 1L;
	
	public Numanthia(int quantity, float price) {
		super(quantity, price);
	}

	public Numanthia(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return WineOntologyVocabulary.NUMANTH;
	}
}