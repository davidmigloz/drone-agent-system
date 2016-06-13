package com.davidflex.supermarket.ontologies.company;

import com.davidflex.supermarket.ontologies.company.actions.AssignOrder;
import com.davidflex.supermarket.ontologies.company.concepts.Order;
import com.davidflex.supermarket.ontologies.company.predicates.InformPosition;
import com.davidflex.supermarket.ontologies.shop.ShopOntology;
import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;

/**
 * Used in internal comunication between shopAgent, personalShopAgents, warehouses and drones.
 */
public class CompanyOntolagy extends BeanOntology implements CompanyOntolagyVocabulary {

    // The singleton instance of this ontology
    private static Ontology instance;

    private CompanyOntolagy() throws BeanOntologyException {
        super(ONTOLOGY_NAME, ShopOntology.getInstance());

        // Add actions
        String pkgname = AssignOrder.class.getName();
        pkgname = pkgname.substring(0, pkgname.lastIndexOf("."));
        add(pkgname);
        // Add concepts
        pkgname = Order.class.getName();
        pkgname = pkgname.substring(0, pkgname.lastIndexOf("."));
        add(pkgname);
        // Add predicates
        pkgname = InformPosition.class.getName();
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
