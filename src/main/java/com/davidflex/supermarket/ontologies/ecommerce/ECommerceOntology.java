package com.davidflex.supermarket.ontologies.ecommerce;

import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;
import com.davidflex.supermarket.ontologies.ecommerce.elements.PurchaseRequest;

/**
 * Based on Giovanni Caire's ECommerceOntology. 
 */
public class ECommerceOntology extends BeanOntology {
	private static final long serialVersionUID = 1L;

	public static final String ONTOLOGY_NAME = "ECommerceOntology";

	// The singleton instance of this ontology
	private static Ontology instance;

	private ECommerceOntology() throws BeanOntologyException {
		super(ONTOLOGY_NAME);

		String pkgname = PurchaseRequest.class.getName();
		pkgname = pkgname.substring(0, pkgname.lastIndexOf("."));
		add(pkgname);
	}
	
	public synchronized final static Ontology getInstance() throws BeanOntologyException {
		if (instance == null) {
			instance = new ECommerceOntology();
		}
		return instance;
	}
}