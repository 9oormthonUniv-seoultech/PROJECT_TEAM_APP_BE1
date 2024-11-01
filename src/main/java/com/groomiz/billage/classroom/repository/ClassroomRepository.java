package com.groomiz.billage.classroom.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.groomiz.billage.classroom.entity.Classroom;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
	@Query("SELECT c FROM Classroom c WHERE c.building.id = :buildingId AND c.floor = :floor AND c.capacity >= :capacity")
	List<Classroom> findByCondition(@Param("buildingId") Long buildingId,
		@Param("floor") Long floor,
		@Param("capacity") int capacity);
}
