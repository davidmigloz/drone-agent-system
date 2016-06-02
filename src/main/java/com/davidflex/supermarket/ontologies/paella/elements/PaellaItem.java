package com.davidflex.supermarket.ontologies.paella.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

public class PaellaItem extends Item {
    private static final String CATEGORY = "Paella";

    public PaellaItem(int quantity, float price) {
        super(quantity, price);
    }

    public String getCategory() {
        return CATEGORY;
    }
}
