package com.davidflex.supermarket.ontologies.shop.concepts.wine;

import com.davidflex.supermarket.ontologies.ecommerce.concepts.Item;
import com.davidflex.supermarket.ontologies.shop.ShopOntologyVocabulary;

public class WineItem extends Item{

    public WineItem() {
    }

    public WineItem(int quantity, float price) {
        super(quantity, price);
    }

    public WineItem(float maxPrice, int quantity) {
        super(maxPrice, quantity);
    }

    public String getCategory() {
        return ShopOntologyVocabulary.WINE_CATEGORY;
    }
}
