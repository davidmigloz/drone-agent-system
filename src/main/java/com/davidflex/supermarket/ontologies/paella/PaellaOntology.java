package com.davidflex.supermarket.ontologies.paella;

import com.davidflex.supermarket.ontologies.ecommerce.ECommerceOntology;
import com.davidflex.supermarket.ontologies.paella.elements.Rice;

import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;

public class PaellaOntology extends BeanOntology implements PaellaOntologyVocabulary {

	private static final long serialVersionUID = 1L;

	// The singleton instance of this ontology
	private static Ontology instance;

	private PaellaOntology() throws BeanOntologyException {
		super(ONTOLOGY_NAME, ECommerceOntology.getInstance());

		String pkgname = Rice.class.getName();
		pkgname = pkgname.substring(0, pkgname.lastIndexOf("."));
		add(pkgname);
	}
	
	public synchronized static Ontology getInstance() throws BeanOntologyException {
		if (instance == null) {
			instance = new PaellaOntology();
		}
		return instance;
	}
}