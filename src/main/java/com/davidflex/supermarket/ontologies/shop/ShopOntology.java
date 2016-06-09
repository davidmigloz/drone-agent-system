package com.davidflex.supermarket.ontologies.shop;

import com.davidflex.supermarket.ontologies.ecommerce.ECommerceOntology;
import com.davidflex.supermarket.ontologies.shop.paella.PaellaItem;
import com.davidflex.supermarket.ontologies.shop.wine.WineItem;
import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;

public class ShopOntology extends BeanOntology implements ShopOntologyVocabulary {

	// The singleton instance of this ontology
	private static Ontology instance;

	private ShopOntology() throws BeanOntologyException {
		super(ONTOLOGY_NAME, ECommerceOntology.getInstance());

		// Add paella items
		String pkgname = PaellaItem.class.getName();
		pkgname = pkgname.substring(0, pkgname.lastIndexOf("."));
		add(pkgname);
		// Add wine items
		pkgname = WineItem.class.getName();
		pkgname = pkgname.substring(0, pkgname.lastIndexOf("."));
		add(pkgname);
	}
	
	public synchronized static Ontology getInstance() throws BeanOntologyException {
		if (instance == null) {
			instance = new ShopOntology();
		}
		return instance;
	}
}