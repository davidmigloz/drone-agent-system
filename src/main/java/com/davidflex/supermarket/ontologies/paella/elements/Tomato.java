package com.davidflex.supermarket.ontologies.paella.elements;

import com.davidflex.supermarket.ontologies.paella.PaellaOntologyVocabulary;

public class Tomato extends PaellaItem {
	private static final long serialVersionUID = 1L;
	
	public Tomato(int quantity, float price) {
		super(quantity, price);
	}

	public Tomato(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return PaellaOntologyVocabulary.TOMATO;
	}
}