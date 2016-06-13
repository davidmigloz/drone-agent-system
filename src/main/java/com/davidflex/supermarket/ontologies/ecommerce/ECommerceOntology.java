package com.davidflex.supermarket.ontologies.ecommerce;

import com.davidflex.supermarket.ontologies.ecommerce.actions.Purchase;
import com.davidflex.supermarket.ontologies.ecommerce.concepts.Item;
import com.davidflex.supermarket.ontologies.ecommerce.predicates.ContactRequest;
import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;

/**
 * ECommerceOntology.
 */
public class ECommerceOntology extends BeanOntology implements ECommerceOntologyVocabulary {

	// The singleton instance of this ontology
	private static Ontology instance;

	private ECommerceOntology() throws BeanOntologyException {
		super(ONTOLOGY_NAME);

		// Add actions
		String pkgname = Purchase.class.getName();
		pkgname = pkgname.substring(0, pkgname.lastIndexOf("."));
		add(pkgname);
		// Add concepts
		pkgname = Item.class.getName();
		pkgname = pkgname.substring(0, pkgname.lastIndexOf("."));
		add(pkgname);
		// Add predicates
		pkgname = ContactRequest.class.getName();
		pkgname = pkgname.substring(0, pkgname.lastIndexOf("."));
		add(pkgname);
	}
	
	public synchronized static Ontology getInstance() throws BeanOntologyException {
		if (instance == null) {
			instance = new ECommerceOntology();
		}
		return instance;
	}
}