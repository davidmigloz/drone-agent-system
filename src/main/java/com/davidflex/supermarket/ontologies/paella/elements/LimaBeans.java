package com.davidflex.supermarket.ontologies.paella.elements;

import com.davidflex.supermarket.ontologies.paella.PaellaOntologyVocabulary;

public class LimaBeans extends PaellaItem {
	private static final long serialVersionUID = 1L;
	
	public LimaBeans(int quantity, float price) {
		super(quantity, price);
	}

	public LimaBeans(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return PaellaOntologyVocabulary.LIMABEAN;
	}
}