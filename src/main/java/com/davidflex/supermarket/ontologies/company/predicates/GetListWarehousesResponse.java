package com.davidflex.supermarket.ontologies.company.predicates;

import com.davidflex.supermarket.ontologies.company.concepts.Warehouse;
import jade.content.Predicate;

import java.util.List;

/**
 * Response with warehouses list.
 */
@SuppressWarnings("unused")
public class GetListWarehousesResponse implements Predicate{
    private List<Warehouse> warehouses;

    public GetListWarehousesResponse() {
    }

    public GetListWarehousesResponse(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }
}
