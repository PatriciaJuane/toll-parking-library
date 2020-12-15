package com.contemporaryparking.tollparkinglibrary.entities;

import io.swagger.annotations.ApiModelProperty;

public class PricingPolicy {

    @ApiModelProperty(
            name = "fixedAmount",
            required = true,
            value = "5.00")
    private double fixedAmount;

    @ApiModelProperty(
            name = "hourPrice",
            required = true,
            value = "1.5")
    private double hourPrice;

    public PricingPolicy(double fixedAmount, double hourPrice) {
        this.fixedAmount = fixedAmount;
        this.hourPrice = hourPrice;
    }

    public double getFixedAmount() {
        return fixedAmount;
    }

    public double getHourPrice() {
        return hourPrice;
    }
}