package com.groomiz.billage.reservation.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.groomiz.billage.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
