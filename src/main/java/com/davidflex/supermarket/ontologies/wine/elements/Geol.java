package com.davidflex.supermarket.ontologies.wine.elements;

import com.davidflex.supermarket.ontologies.wine.WineOntologyVocabulary;

public class Geol extends WineItem {
	private static final long serialVersionUID = 1L;
	
	public Geol(int quantity, float price) {
		super(quantity, price);
	}

	@Override
	public String toString() {
		return WineOntologyVocabulary.GEOL;
	}
}