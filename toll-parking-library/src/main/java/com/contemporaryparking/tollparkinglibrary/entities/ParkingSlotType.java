package com.contemporaryparking.tollparkinglibrary.entities;

public enum ParkingSlotType {
    STANDARD(0) {
        @Override
        public boolean isForElectricCar() {
            return false;
        }
    },
    ELECTRIC_CAR_20KW(20) {
        @Override
        public boolean isForElectricCar() {
            return true;
        }
    },
    ELECTRIC_CAR_50KW(50) {
        @Override
        public boolean isForElectricCar() {
            return true;
        }
    };

    private int powerSupply;

    public boolean isForElectricCar() {
        return false;
    }

    public int getPowerSupply() {
        return powerSupply;
    }

    ParkingSlotType(int powerSupply) {
        this.powerSupply = powerSupply;
    }
}
