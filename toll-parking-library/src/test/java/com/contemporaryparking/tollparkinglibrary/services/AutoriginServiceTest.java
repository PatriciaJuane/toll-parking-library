package com.contemporaryparking.tollparkinglibrary.services;

import com.contemporaryparking.tollparkinglibrary.entities.ParkingSlotType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AutoriginServiceTest {

    private AutoriginService autoriginService = new AutoriginService();

    @Test
    public void testRetrieveParkingSlotType() {
        // prepare
        String plateNumber = "AB 123 CD";

        // execute
        ParkingSlotType parkingSlotType = autoriginService.retrieveParkingSlotType(plateNumber);

        // asserts
        Assert.assertNotNull("Never null is returned by mocked service", parkingSlotType);
        Assert.assertTrue(
                "Parking slot type is existing in the types",
                Arrays.asList(ParkingSlotType.values()).contains(parkingSlotType));
    }
}
