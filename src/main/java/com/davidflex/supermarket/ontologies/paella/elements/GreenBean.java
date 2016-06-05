package com.davidflex.supermarket.ontologies.paella.elements;

import com.davidflex.supermarket.ontologies.paella.PaellaOntologyVocabulary;

public class GreenBean extends PaellaItem {
	private static final long serialVersionUID = 1L;

	public GreenBean(int quantity, float price) {
		super(quantity, price);
	}

	public GreenBean(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return PaellaOntologyVocabulary.GREENBEA;
	}
}