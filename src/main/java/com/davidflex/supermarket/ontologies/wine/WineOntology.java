package com.davidflex.supermarket.ontologies.wine;

import com.davidflex.supermarket.ontologies.ecommerce.ECommerceOntology;
import com.davidflex.supermarket.ontologies.wine.elements.AdegaDoMoucho;

import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;

public class WineOntology extends BeanOntology implements WineOntologyVocabulary {

	private static final long serialVersionUID = 1L;

	// The singleton instance of this ontology
	private static Ontology instance;

	private WineOntology() throws BeanOntologyException {
		super(ONTOLOGY_NAME, ECommerceOntology.getInstance());

		String pkgname = AdegaDoMoucho.class.getName();
		pkgname = pkgname.substring(0, pkgname.lastIndexOf("."));
		add(pkgname);
	}
	
	public synchronized static Ontology getInstance() throws BeanOntologyException {
		if (instance == null) {
			instance = new WineOntology();
		}
		return instance;
	}
}