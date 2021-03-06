package com.contemporaryparking.tollparkinglibrary.repository;

import com.contemporaryparking.tollparkinglibrary.entities.ParkingBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingBillRepository extends JpaRepository<ParkingBill, Long> {
}
