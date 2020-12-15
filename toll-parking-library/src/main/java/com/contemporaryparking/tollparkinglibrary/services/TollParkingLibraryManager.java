package com.contemporaryparking.tollparkinglibrary.services;

import com.contemporaryparking.tollparkinglibrary.config.TollParkingLibraryConfig;
import com.contemporaryparking.tollparkinglibrary.entities.ParkingBill;
import com.contemporaryparking.tollparkinglibrary.entities.ParkingSlot;
import com.contemporaryparking.tollparkinglibrary.entities.ParkingSlotType;
import com.contemporaryparking.tollparkinglibrary.entities.PricingPolicy;
import com.contemporaryparking.tollparkinglibrary.repository.ParkingBillRepository;
import com.contemporaryparking.tollparkinglibrary.repository.ParkingSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.IntStream;

@Component
public class TollParkingLibraryManager {

    private DebitService debitService;

    private AutoriginService autoriginService;

    private ParkingSlotRepository parkingSlotRepository;

    private ParkingBillRepository parkingBillRepository;

    private boolean initialized = false;

    @Autowired
    public TollParkingLibraryManager(
            DebitService debitService,
            AutoriginService autoriginService,
            ParkingSlotRepository parkingSlotRepository,
            ParkingBillRepository parkingBillRepository) {
        this.debitService = debitService;
        this.autoriginService = autoriginService;
        this.parkingSlotRepository = parkingSlotRepository;
        this.parkingBillRepository = parkingBillRepository;
    }

    public TollParkingLibraryConfig initialize(TollParkingLibraryConfig tollParkingLibraryConfig) {
        if (!initialized) {
            int numberOfStandardParkingSlot = tollParkingLibraryConfig.getNumOfStandardParkingSlot();
            int numberOfElectricCar20KWParkingSlot = tollParkingLibraryConfig.getNumOfElectricCar20KWParkingSlot();
            int numberOfElectricCar50KWParkingSlot = tollParkingLibraryConfig.getNumOfElectricCar50KWParkingSlot();
            IntStream.rangeClosed(1, numberOfStandardParkingSlot)
                    .forEach(
                            i -> parkingSlotRepository.save(new ParkingSlot(ParkingSlotType.STANDARD, true)));

            IntStream.rangeClosed(1, numberOfElectricCar20KWParkingSlot)
                    .forEach(
                            i -> parkingSlotRepository.save(
                                            new ParkingSlot(ParkingSlotType.ELECTRIC_CAR_20KW, true)));

            IntStream.rangeClosed(1, numberOfElectricCar50KWParkingSlot)
                    .forEach(
                            i -> parkingSlotRepository.save(
                                            new ParkingSlot(ParkingSlotType.ELECTRIC_CAR_50KW, true)));

            debitService.updatePricingPolicy(tollParkingLibraryConfig.getPricingPolicy());
            initialized = true;

            return tollParkingLibraryConfig;
        }

        return null;
    }

    public boolean updatePricingPolicy(PricingPolicy pricingPolicy) {
        return debitService.updatePricingPolicy(pricingPolicy);
    }

    public synchronized Optional<ParkingSlot> getParkingSlot(String plateNumber) {
        ParkingSlotType parkingSlotType = autoriginService.retrieveParkingSlotType(plateNumber);
        Optional<ParkingSlot> firstParkingSlot = parkingSlotRepository.findAll().stream()
                        .filter(ps -> ps.getParkingSlotType().equals(parkingSlotType) && ps.isFree())
                        .findFirst();
        if (firstParkingSlot.isPresent()) {
            ParkingSlot parkingSlot = firstParkingSlot.get();
            parkingSlot.setFree(false);
            parkingSlotRepository.save(parkingSlot);
            parkingBillRepository.save(new ParkingBill(plateNumber, parkingSlot, LocalDateTime.now()));
        }

        return firstParkingSlot;
    }

    public synchronized Optional<ParkingBill> leaveParking(String plateNumber) {
        Optional<ParkingBill> firstParkingBill = parkingBillRepository.findAll().stream()
                            .filter(pb -> pb.getPlateNumber().equals(plateNumber) && pb.getEnd() == null)
                            .findFirst();
        if (firstParkingBill.isPresent()) {
            ParkingBill parkingBill = firstParkingBill.get();
            parkingBill.setEnd(LocalDateTime.now());
            parkingSlotRepository.findById(parkingBill.getParkingSlot().getId()).get().setFree(true);
            debitService.handleParkingBill(parkingBill);
        }

        return firstParkingBill;
    }
}
