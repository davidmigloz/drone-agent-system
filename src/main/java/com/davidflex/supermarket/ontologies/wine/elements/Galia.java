package com.davidflex.supermarket.ontologies.wine.elements;

import com.davidflex.supermarket.ontologies.wine.WineOntologyVocabulary;

public class Galia extends WineItem {
	private static final long serialVersionUID = 1L;
	
	public Galia(int quantity, float price) {
		super(quantity, price);
	}

	public Galia(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return WineOntologyVocabulary.GALIA;
	}
}