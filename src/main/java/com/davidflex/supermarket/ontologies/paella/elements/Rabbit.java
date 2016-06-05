package com.davidflex.supermarket.ontologies.paella.elements;

import com.davidflex.supermarket.ontologies.paella.PaellaOntologyVocabulary;

public class Rabbit extends PaellaItem {
	private static final long serialVersionUID = 1L;
	
	public Rabbit(int quantity, float price) {
		super(quantity, price);
	}

	public Rabbit(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return PaellaOntologyVocabulary.RABBIT;
	}
}