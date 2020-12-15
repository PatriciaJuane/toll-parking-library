package com.contemporaryparking.tollparkinglibrary.controller;

import static org.hamcrest.Matchers.is;
import com.contemporaryparking.tollparkinglibrary.config.TollParkingLibraryConfig;
import com.contemporaryparking.tollparkinglibrary.entities.ParkingBill;
import com.contemporaryparking.tollparkinglibrary.entities.ParkingSlot;
import com.contemporaryparking.tollparkinglibrary.entities.ParkingSlotType;
import com.contemporaryparking.tollparkinglibrary.entities.PricingPolicy;
import com.contemporaryparking.tollparkinglibrary.repository.ParkingBillRepository;
import com.contemporaryparking.tollparkinglibrary.repository.ParkingSlotRepository;
import com.contemporaryparking.tollparkinglibrary.services.AutoriginService;
import com.contemporaryparking.tollparkinglibrary.services.DebitService;
import com.contemporaryparking.tollparkinglibrary.services.TollParkingLibraryManager;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TollParkingLibraryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TollParkingLibraryManager tollParkingLibraryManager;

    @MockBean
    private DebitService debitService;

    @MockBean
    private AutoriginService autoriginService;

    @MockBean
    private ParkingSlotRepository parkingSlotRepository;

    @MockBean
    private ParkingBillRepository parkingBillRepository;

    @Test
    public void testEndpoints() throws Exception {
        // prepare
        PricingPolicy pricingPolicy = new PricingPolicy(5, 1.2);
        PricingPolicy newPricingPolicy = new PricingPolicy(7, 1.0);
        TollParkingLibraryConfig tollParkingConfig = new TollParkingLibraryConfig(10, 10, 10, pricingPolicy);
        String plateNumber = "AB 123 CD";
        ParkingSlot parkingSlot = new ParkingSlot(ParkingSlotType.ELECTRIC_CAR_50KW, false);
        parkingSlot.setId(1L);
        Optional<ParkingSlot> optionalParkingSlot = Optional.of(parkingSlot);
        ParkingBill parkingBill =
                new ParkingBill("AB 123 CD", parkingSlot, LocalDateTime.of(2012, 7, 1, 12, 0));
        parkingBill.setId(1L);
        parkingBill.setEnd(LocalDateTime.of(2012, 7, 1, 13, 45));
        parkingBill.setPrice(7.1);
        Optional<ParkingBill> optionalParkingBill = Optional.of(parkingBill);
        doReturn(tollParkingConfig).when(tollParkingLibraryManager).initialize(any());
        doReturn(true).when(tollParkingLibraryManager).updatePricingPolicy(any());
        doReturn(optionalParkingSlot).when(tollParkingLibraryManager).getParkingSlot(plateNumber);
        doReturn(optionalParkingBill).when(tollParkingLibraryManager).leaveParking(plateNumber);

        // execute
        // initialize
        mvc.perform(
                MockMvcRequestBuilders.post("/initialize")
                        .content(toJson(tollParkingConfig))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfStandardParkingSlot", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElectricCar20KWParkingSlot", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElectricCar50KWParkingSlot", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pricingPolicy.fixedAmount", is(5.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pricingPolicy.hourPrice", is(1.2)));
        // update pricing policy
        mvc.perform(
                MockMvcRequestBuilders.put("/pricingpolicy")
                        .content(toJson(newPricingPolicy))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fixedAmount", is(7.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hourPrice", is(1.0)));
        // enter parking
        mvc.perform(
                MockMvcRequestBuilders.get("/enterparking/AB 123 CD")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.free", is(false)));
        // leave parking
        mvc.perform(
                MockMvcRequestBuilders.get("/leaveparking/AB 123 CD")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.plateNumber", is("AB 123 CD")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", is(7.1)));
    }

    private byte[] toJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}
