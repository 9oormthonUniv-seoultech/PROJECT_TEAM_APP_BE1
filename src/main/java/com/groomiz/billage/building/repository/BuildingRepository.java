package com.groomiz.billage.building.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.groomiz.billage.building.entity.Building;

public interface BuildingRepository extends JpaRepository<Building, Long> {
	// 특정 날짜에 해당하는 건물의 예약 수 계산
	@Query("SELECT COUNT(r) FROM Reservation r WHERE r.classroom.building.id = :buildingId AND r.applyDate = :date")
	long countReservationsByBuildingAndDate(@Param("buildingId") Long buildingId, @Param("date") LocalDate date);

	// 건물에 속한 강의실 수 계산
	@Query("SELECT COUNT(c) FROM Classroom c WHERE c.building.id = :buildingId")
	long countClassroomsByBuilding(@Param("buildingId") Long buildingId);

	// 수용 인원이 headcount 이상인 강의실이 있는 건물 필터링
	@Query("SELECT DISTINCT b FROM Building b JOIN Classroom c ON c.building.id = b.id WHERE c.capacity >= :headcount")
	List<Building> findBuildingsByClassroomCapacity(@Param("headcount") Integer headcount);
}
