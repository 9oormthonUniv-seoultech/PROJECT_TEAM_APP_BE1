package com.groomiz.billage.building.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.springframework.stereotype.Service;

import com.groomiz.billage.building.dto.response.BuildingListResponse;
import com.groomiz.billage.building.entity.Building;
import com.groomiz.billage.building.exception.BuildingErrorCode;
import com.groomiz.billage.building.exception.BuildingException;
import com.groomiz.billage.building.repository.BuildingRepository;
import com.groomiz.billage.classroom.repository.ClassroomRepository;
import com.groomiz.billage.reservation.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuildingService {

	private final BuildingRepository buildingRepository;
	private final ReservationRepository reservationRepository;
	private final ClassroomRepository classroomRepository;

	public List<BuildingListResponse> findAllBuildings(LocalDate date, Integer count) {

		List<Building> buildings = buildingRepository.findAll();

		if (buildings.isEmpty()) {
			throw new BuildingException(BuildingErrorCode.BUILDING_NOT_FOUND);
		}

		return buildings.stream()
			.map(building -> {
				long totalReservations = reservationRepository.countReservationsByBuildingAndDate(building.getId(), date);
				long totalClassrooms = classroomRepository.countClassroomsByBuilding(building.getId());

				Congestion congestion = CongestionCalculator.calculateCongestion(totalReservations, totalClassrooms);

				List<Long> floors = generateFloors(building.getStartFloor(), building.getEndFloor());

				BuildingListResponse response = new BuildingListResponse();

				response.setBuildingId(building.getId());
				response.setBuildingName(building.getName());
				response.setBuildingNumber(building.getNumber());
				response.setCongestion(congestion);
				response.setFloors(floors);

				return response;
			})
			.collect(Collectors.toList());
	}

	private List<Long> generateFloors(Long startFloor, Long endFloor) {
		return LongStream.rangeClosed(startFloor, endFloor)
			.boxed()
			.collect(Collectors.toList());
	}
}
