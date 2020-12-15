package com.contemporaryparking.tollparkinglibrary.repository;

import com.contemporaryparking.tollparkinglibrary.entities.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
}
