package com.urbanbasket.vrp.resource;

class VrpParameters {
    private String[] customerLocations;
    private String depot;
    private Integer carCount;

    public VrpParameters() {

    }

    public VrpParameters(String[] customerLocations, String depot, Integer carCount) {
        this.setCustomerLocations(customerLocations);
        this.setDepot(depot);
        this.setCarCount(carCount);
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

    public Integer getCarCount() {
        return carCount;
    }

    public void setCarCount(Integer carCount) {
        this.carCount = carCount;
    }
}
