package com.davidflex.supermarket.ontologies.wine.elements;

import com.davidflex.supermarket.ontologies.wine.WineOntologyVocabulary;

public class Gramona extends WineItem {
	private static final long serialVersionUID = 1L;
	
	public Gramona(int quantity, float price) {
		super(quantity, price);
	}

	@Override
	public String toString() {
		return WineOntologyVocabulary.GRAMONA;
	}
}