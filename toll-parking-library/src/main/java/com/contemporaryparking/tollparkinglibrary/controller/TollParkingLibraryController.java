package com.contemporaryparking.tollparkinglibrary.controller;

import com.contemporaryparking.tollparkinglibrary.config.TollParkingLibraryConfig;
import com.contemporaryparking.tollparkinglibrary.entities.ParkingBill;
import com.contemporaryparking.tollparkinglibrary.entities.ParkingSlot;
import com.contemporaryparking.tollparkinglibrary.entities.PricingPolicy;
import com.contemporaryparking.tollparkinglibrary.services.TollParkingLibraryManager;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Api(value = "TollParkingLibraryController")
@RestController("/api/v1")
public class TollParkingLibraryController {

    private TollParkingLibraryManager tollParkingLibraryManager;

    @Autowired
    public TollParkingLibraryController(TollParkingLibraryManager tollParkingLibraryManager) {
        this.tollParkingLibraryManager = tollParkingLibraryManager;
    }

    @ApiOperation(value = "Initialize toll parking library", response = TollParkingLibraryConfig.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK: Successfully initialized"),
                    @ApiResponse(code = 403, message = "Forbidden: Already initialized")
            })
    @PostMapping("/initialize")
    public ResponseEntity<TollParkingLibraryConfig> initializeTollParking(
            @RequestBody TollParkingLibraryConfig tollParkingConfig) throws Exception {
        TollParkingLibraryConfig tpc =
                Optional.ofNullable(tollParkingLibraryManager.initialize(tollParkingConfig))
                        .orElseThrow(
                                () -> new Exception("Toll parking library has been already initialized"));
        return ResponseEntity.ok(tpc);
    }

    @ApiOperation(
            value = "Change pricing policy for toll parking library",
            response = PricingPolicy.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK: Pricing policy changed"),
                    @ApiResponse(code = 403, message = "Forbidden: Invalid pricing policy")
            })
    @PutMapping("/pricingpolicy")
    public ResponseEntity<PricingPolicy> updatePricingPolicy(@RequestBody PricingPolicy pricingPolicy)
            throws Exception {
        if (tollParkingLibraryManager.updatePricingPolicy(pricingPolicy)) {
            return ResponseEntity.ok(pricingPolicy);
        } else {
            throw new Exception("Invalid pricing policy rejected");
        }
    }

    @ApiOperation(value = "Get first available parking slot", response = ParkingSlot.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK: Parking slot retrieved"),
                    @ApiResponse(code = 404, message = "Not found: No available parking slot")
            })
    @GetMapping("/enterparking/{plateNumber}")
    public ResponseEntity<ParkingSlot> enterParking(@PathVariable("plateNumber") String plateNumber)
            throws Exception {
        ParkingSlot parkingSlot =
                tollParkingLibraryManager
                        .getParkingSlot(plateNumber)
                        .orElseThrow(
                                () -> new Exception(
                                                "No available parking slot has been found for this car: " + plateNumber));

        return ResponseEntity.ok(parkingSlot);
    }

    @ApiOperation(value = "Leave parking slot and return the bill", response = ParkingBill.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK: Parking bill retrieved"),
                    @ApiResponse(code = 404, message = "Not found: Parking bill not found")
            })
    @GetMapping("/leaveparking/{plateNumber}")
    public ResponseEntity<ParkingBill> leaveParking(@PathVariable("plateNumber") String plateNumber)
            throws Exception {
        ParkingBill parkingBill = tollParkingLibraryManager
                        .leaveParking(plateNumber)
                        .orElseThrow(
                                () -> new Exception(
                                                "No parking bill has been found for this car: " + plateNumber));

        return ResponseEntity.ok(parkingBill);
    }

}
