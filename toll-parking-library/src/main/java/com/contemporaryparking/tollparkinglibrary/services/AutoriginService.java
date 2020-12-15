package com.contemporaryparking.tollparkinglibrary.services;

import com.contemporaryparking.tollparkinglibrary.entities.ParkingSlotType;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AutoriginService {

    public ParkingSlotType retrieveParkingSlotType(String plateNumber) {
        Random random = new Random();
        return ParkingSlotType.values()[random.nextInt(ParkingSlotType.values().length)];
    }

}
