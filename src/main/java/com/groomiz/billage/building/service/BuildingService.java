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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuildingService {

	private final BuildingRepository buildingRepository;

	public List<BuildingListResponse> findAllBuildings(LocalDate date, Integer count) {

		List<Building> buildings = buildingRepository.findBuildingsByClassroomCapacity(count);

		if (buildings.isEmpty()) {
			throw new BuildingException(BuildingErrorCode.BUILDING_NOT_FOUND);
		}

		return buildings.stream()
			.map(building -> {
				long totalReservations = buildingRepository.countReservationsByBuildingAndDate(building.getId(), date);
				long totalClassrooms = buildingRepository.countClassroomsByBuilding(building.getId());


				List<Long> floors = generateFloors(building.getStartFloor(), building.getEndFloor());

				BuildingListResponse response = BuildingListResponse.builder()
					.buildingId(building.getId())
					.buildingName(building.getName())
					.buildingNumber(building.getNumber())
					.floors(floors)
					.build();

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
