package com.davidflex.supermarket.ontologies.wine.elements;

import com.davidflex.supermarket.ontologies.wine.WineOntologyVocabulary;

public class Plazuela extends WineItem {
	private static final long serialVersionUID = 1L;
	
	public Plazuela(int quantity, float price) {
		super(quantity, price);
	}

	public Plazuela(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return WineOntologyVocabulary.PLAZUEL;
	}
}