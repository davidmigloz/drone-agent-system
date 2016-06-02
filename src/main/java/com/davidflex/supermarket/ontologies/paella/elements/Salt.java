package com.davidflex.supermarket.ontologies.paella.elements;

import com.davidflex.supermarket.ontologies.paella.PaellaOntologyVocabulary;

public class Salt extends PaellaItem {
	private static final long serialVersionUID = 1L;
	
	public Salt(int quantity, float price) {
		super(quantity, price);
	}

	@Override
	public String toString() {
		return PaellaOntologyVocabulary.SALT;
	}
}