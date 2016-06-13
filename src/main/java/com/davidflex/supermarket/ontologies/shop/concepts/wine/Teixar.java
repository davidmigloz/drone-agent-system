package com.davidflex.supermarket.ontologies.shop.concepts.wine;

import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;

@SuppressWarnings("unused")
public class Teixar extends WineItem {

	public Teixar() {
	}

	public Teixar(int quantity, float price) {
		super(quantity, price);
	}

	public Teixar(float maxPrice, int quantity) {
		super(maxPrice, quantity);
	}

	@Override
	public String toString() {
		return ShopOntologyVocabulary.TEIXAR;
	}
}