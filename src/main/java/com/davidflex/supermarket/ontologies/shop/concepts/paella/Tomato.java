package com.davidflex.supermarket.ontologies.shop.concepts.paella;

import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;

@SuppressWarnings("unused")
public class Tomato extends PaellaItem {

	public Tomato() {
	}

	public Tomato(int quantity, float price) {
		super(quantity, price);
	}

	public Tomato(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return ShopOntologyVocabulary.TOMATO;
	}
}