package com.contemporaryparking.tollparkinglibrary.services;

import com.contemporaryparking.tollparkinglibrary.entities.ParkingBill;
import com.contemporaryparking.tollparkinglibrary.entities.PricingPolicy;
import com.contemporaryparking.tollparkinglibrary.repository.ParkingBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;

@Service
public class DebitService {

    private PricingPolicy pricingPolicy;

    private ParkingBillRepository parkingBillRepository;

    @Autowired
    public DebitService(ParkingBillRepository parkingBillRepository) {
        this.parkingBillRepository = parkingBillRepository;
    }

    public boolean updatePricingPolicy(PricingPolicy pricingPolicy) {
        if (pricingPolicy.getFixedAmount() >= 0 && pricingPolicy.getHourPrice() > 0) {
            this.pricingPolicy = pricingPolicy;
            return true;
        }

        return false;
    }

    public ParkingBill handleParkingBill(ParkingBill parkingBill) {
        double price = pricingPolicy.getFixedAmount() + pricingPolicy.getHourPrice() * parkingHours(parkingBill);
        parkingBill.setPrice(price);

        return parkingBillRepository.save(parkingBill);
    }

    private double parkingHours(ParkingBill parkingBill) {
        return parkingBill.getStart().until(parkingBill.getEnd(), ChronoUnit.MINUTES) / 60.;
    }
}
