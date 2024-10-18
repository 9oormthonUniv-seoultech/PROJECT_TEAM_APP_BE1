package com.groomiz.billage.reservation.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.groomiz.billage.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	@Query("SELECT COUNT(r) FROM Reservation r WHERE r.classroom.building.id = :buildingId AND r.applyDate = :date")
	long countReservationsByBuildingAndDate(@Param("buildingId") Long buildingId, @Param("date") LocalDate date);
}
