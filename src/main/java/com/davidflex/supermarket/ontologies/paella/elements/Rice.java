package com.davidflex.supermarket.ontologies.paella.elements;

import com.davidflex.supermarket.ontologies.paella.PaellaOntologyVocabulary;

public class Rice extends PaellaItem{
	private static final long serialVersionUID = 1L;
	
	public Rice(int quantity, float price) {
		super(quantity, price);
	}

	public Rice(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return PaellaOntologyVocabulary.RICE;
	}
}