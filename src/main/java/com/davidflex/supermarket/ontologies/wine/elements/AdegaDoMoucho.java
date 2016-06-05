package com.davidflex.supermarket.ontologies.wine.elements;

import com.davidflex.supermarket.ontologies.wine.WineOntologyVocabulary;

public class AdegaDoMoucho extends WineItem {
	private static final long serialVersionUID = 1L;
	
	public AdegaDoMoucho(int quantity, float price) {
		super(quantity, price);
	}

	public AdegaDoMoucho(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return WineOntologyVocabulary.ADEGA;
	}
}