package com.davidflex.supermarket.ontologies.wine.elements;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;

public class WineItem extends Item{
    private static final String CATEGORY = "Wine";

    public WineItem(int quantity, float price) {
        super(quantity, price);
    }

    public WineItem(float maxPrice, int quantity) {
        super(maxPrice, quantity);
    }

    public String getCategory() {
        return CATEGORY;
    }
}
