package com.davidflex.supermarket.ontologies.company;

import com.davidflex.supermarket.ontologies.company.elements.Order;
import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;

/**
 * Used in internal comunication between shopAgent, personalShopAgents, warehouses and drones.
 */
public class CompanyOntolagy extends BeanOntology implements CompanyOntolagyVocabulary {
    private static final long serialVersionUID = 1L;

    // The singleton instance of this ontology
    private static Ontology instance;

    private CompanyOntolagy() throws BeanOntologyException {
        super(ONTOLOGY_NAME);

        String pkgname = Order.class.getName();
        pkgname = pkgname.substring(0, pkgname.lastIndexOf("."));
        add(pkgname);
    }

    public synchronized static Ontology getInstance() throws BeanOntologyException {
        if (instance == null) {
            instance = new CompanyOntolagy();
        }
        return instance;
    }
}
