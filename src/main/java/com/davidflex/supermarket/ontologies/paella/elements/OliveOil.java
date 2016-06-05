package com.davidflex.supermarket.ontologies.paella.elements;

import com.davidflex.supermarket.ontologies.paella.PaellaOntologyVocabulary;

public class OliveOil extends PaellaItem {
	private static final long serialVersionUID = 1L;
	
	public OliveOil(int quantity, float price) {
		super(quantity, price);
	}

	public OliveOil(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return PaellaOntologyVocabulary.OLIVEOIL;
	}
}