package com.contemporaryparking.tollparkinglibrary.entities;

import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ParkingSlots")
public class ParkingSlot {

    @ApiModelProperty(
            name = "id",
            required = true,
            value = "123",
            notes = "The database generated parking slot ID")
    private long id;

    @ApiModelProperty(
            name = "parkingSlotType",
            required = true,
            value = "STANDARD/ELECTRIC_CAR_20KW/ELECTRIC_CAR_50KW")
    @Enumerated(EnumType.STRING)
    @Column(length = 17)
    private ParkingSlotType parkingSlotType;

    @ApiModelProperty(
            name = "free",
            required = true,
            value = "false/true",
            notes = "parking slot availability")
    private boolean free;

    public ParkingSlot() {
    }

    public ParkingSlot(ParkingSlotType parkingSlotType, boolean free) {
        this.parkingSlotType = parkingSlotType;
        this.free = free;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ParkingSlotType getParkingSlotType() {
        return parkingSlotType;
    }

    public void setParkingSlotType(ParkingSlotType parkingSlotType) {
        this.parkingSlotType = parkingSlotType;
    }

    @Column(name = "free", nullable = false)
    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }
}