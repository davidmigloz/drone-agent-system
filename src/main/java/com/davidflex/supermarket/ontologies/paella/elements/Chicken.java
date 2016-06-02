package com.davidflex.supermarket.ontologies.paella.elements;

import com.davidflex.supermarket.ontologies.paella.PaellaOntologyVocabulary;

public class Chicken extends PaellaItem {
	private static final long serialVersionUID = 1L;
	
	public Chicken(int quantity, float price) {
		super(quantity, price);
	}

	@Override
	public String toString() {
		return PaellaOntologyVocabulary.CHICKEN;
	}
}