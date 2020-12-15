package com.contemporaryparking.tollparkinglibrary.entities;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ParkingBill")
public class ParkingBill {

    @ApiModelProperty(
            name = "id",
            required = true,
            value = "123")
    private long id;

    @ApiModelProperty(
            name = "plateNumber",
            required = true,
            value = "AB 123 CD")
    private String plateNumber;

    @ApiModelProperty(name = "parkingSlot", required = true, value = "123")
    private ParkingSlot parkingSlot;

    @ApiModelProperty(
            name = "hourPrice",
            required = true,
            value = "7.5")
    private double price;

    @ApiModelProperty(
            name = "start",
            required = true,
            value = "2020-05-10T03:43:17.015")
    private LocalDateTime start;

    @ApiModelProperty(
            name = "end",
            required = false,
            value = "2020-05-10T03:43:18.115")
    private LocalDateTime end;

    public ParkingBill(String plateNumber, ParkingSlot parkingSlot, LocalDateTime start) {
        this.plateNumber = plateNumber;
        this.parkingSlot = parkingSlot;
        this.start = start;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "PARKINGSLOTS_ID")
    public ParkingSlot getParkingSlot() {
        return parkingSlot;
    }

    public void setParkingSlot(ParkingSlot parkingSlot) {
        this.parkingSlot = parkingSlot;
    }

    @Column(name = "plate_number", nullable = false)
    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    @Column(name = "price", nullable = false)
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Column(name = "start_time", nullable = false)
    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    @Column(name = "end_time", nullable = false)
    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}