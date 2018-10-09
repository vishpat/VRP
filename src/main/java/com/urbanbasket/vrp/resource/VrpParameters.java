package com.urbanbasket.vrp.resource;

public class VrpParameters {
    private String[] customerLocations;
    private String depot;

    public VrpParameters() {

    }

    public VrpParameters(String customerLocations[], String depot) {
        this.setCustomerLocations(customerLocations);
        this.setDepot(depot);
    }

    public String[] getCustomerLocations() {
        return customerLocations;
    }

    public void setCustomerLocations(String[] customerLocations) {
        this.customerLocations = customerLocations;
    }

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }
}
