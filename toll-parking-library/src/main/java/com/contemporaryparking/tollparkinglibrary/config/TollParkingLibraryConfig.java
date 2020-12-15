package com.contemporaryparking.tollparkinglibrary.config;

import com.contemporaryparking.tollparkinglibrary.entities.PricingPolicy;
import io.swagger.annotations.ApiModelProperty;

public class TollParkingLibraryConfig {

    @ApiModelProperty(
            name = "numOfStandardParkingSlot",
            required = true,
            value = "100")
    private int numOfStandardParkingSlot;

    @ApiModelProperty(
            name = "numOfElectricCar20KWParkingSlot",
            required = true,
            value = "100")
    private int numOfElectricCar20KWParkingSlot;

    @ApiModelProperty(
            name = "numOfElectricCar50KWParkingSlot",
            required = true,
            value = "100")
    private int numOfElectricCar50KWParkingSlot;

    @ApiModelProperty(name = "pricingPolicy", required = true)
    private PricingPolicy pricingPolicy;

    public TollParkingLibraryConfig(
            int numberOfStandardParkingSlot,
            int numberOfElectricCar20KWParkingSlot,
            int numberOfElectricCar50KWParkingSlot,
            PricingPolicy pricingPolicy) {
        this.numOfStandardParkingSlot = numberOfStandardParkingSlot;
        this.numOfElectricCar20KWParkingSlot = numberOfElectricCar20KWParkingSlot;
        this.numOfElectricCar50KWParkingSlot = numberOfElectricCar50KWParkingSlot;
        this.pricingPolicy = pricingPolicy;
    }

    public int getNumOfStandardParkingSlot() {
        return numOfStandardParkingSlot;
    }

    public int getNumOfElectricCar20KWParkingSlot() {
        return numOfElectricCar20KWParkingSlot;
    }

    public int getNumOfElectricCar50KWParkingSlot() {
        return numOfElectricCar50KWParkingSlot;
    }

    public PricingPolicy getPricingPolicy() {
        return pricingPolicy;
    }
}
