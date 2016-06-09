package com.davidflex.supermarket.ontologies.shop.paella;

import com.davidflex.supermarket.ontologies.ecommerce.elements.Item;
import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;

public class PaellaItem extends Item {

    public PaellaItem() {
    }

    public PaellaItem(int quantity, float price) {
        super(quantity, price);
    }

    public PaellaItem(float maxPrice, int quantity) {
        super(maxPrice, quantity);
    }

    public String getCategory() {
        return ShopOntologyVocabulary.PAELLA_CATEGORY;
    }
}
