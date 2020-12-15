package com.contemporaryparking.tollparkinglibrary.services;

import com.contemporaryparking.tollparkinglibrary.entities.ParkingBill;
import com.contemporaryparking.tollparkinglibrary.entities.ParkingSlot;
import com.contemporaryparking.tollparkinglibrary.entities.PricingPolicy;
import com.contemporaryparking.tollparkinglibrary.repository.ParkingBillRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DebitServiceTest {

    @Mock
    private ParkingBillRepository parkingBillRepository;

    private DebitService debitService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(parkingBillRepository.save(any()))
                .thenAnswer(
                        new Answer<ParkingBill>() {
                            @Override
                            public ParkingBill answer(InvocationOnMock invocation) throws Throwable {
                                Object[] args = invocation.getArguments();
                                return (ParkingBill) args[0];
                            }
                        });

        debitService = new DebitService(parkingBillRepository);
    }

    @Test
    public void testRetrieveParkingSlotType_ValidPricingPolicy() {
        // prepare
        PricingPolicy pricingPolicy = new PricingPolicy(5, 1.2);
        ParkingSlot parkingSlot = new ParkingSlot();
        ParkingBill parkingBill =
                new ParkingBill("AB 123 CD", parkingSlot, LocalDateTime.of(2012, 7, 1, 12, 0));
        parkingBill.setEnd(LocalDateTime.of(2012, 7, 1, 13, 45));

        // execute
        boolean updated = debitService.updatePricingPolicy(pricingPolicy);
        parkingBill = debitService.handleParkingBill(parkingBill);

        // asserts
        Assert.assertTrue("Pricing policy has been updated", updated);
        Assert.assertNotNull("Parking bill is not null", parkingBill);
        Assert.assertEquals(7.1, parkingBill.getPrice(), 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void testRetrieveParkingSlotType_InvalidPricingPolicy() {
        // prepare
        PricingPolicy pricingPolicy = new PricingPolicy(0.0, 0.0);
        ParkingSlot parkingSlot = new ParkingSlot();
        ParkingBill parkingBill =
                new ParkingBill("AB 123 CD", parkingSlot, LocalDateTime.of(2012, 7, 1, 12, 0));
        parkingBill.setEnd(LocalDateTime.of(2012, 7, 1, 13, 45));

        // execute
        boolean updated = debitService.updatePricingPolicy(pricingPolicy);

        // assert
        Assert.assertFalse("Pricing policy has been updated", updated);

        // execute
        debitService.handleParkingBill(parkingBill);
    }
}
