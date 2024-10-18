package com.groomiz.billage.classroom.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.groomiz.billage.classroom.entity.Classroom;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

	@Query("SELECT COUNT(c) FROM Classroom c WHERE c.building.id = :buildingId")
	long countClassroomsByBuilding(@Param("buildingId") Long buildingId);
}
