package com.groomiz.billage.classroom.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.groomiz.billage.building.entity.Building;
import com.groomiz.billage.building.exception.BuildingErrorCode;
import com.groomiz.billage.building.exception.BuildingException;
import com.groomiz.billage.building.repository.BuildingRepository;
import com.groomiz.billage.classroom.dto.ReservationTime;
import com.groomiz.billage.classroom.dto.request.ClassroomListRequest;
import com.groomiz.billage.classroom.dto.response.ClassroomListResponse;
import com.groomiz.billage.classroom.entity.Classroom;
import com.groomiz.billage.classroom.exception.ClassroomErrorCode;
import com.groomiz.billage.classroom.exception.ClassroomException;
import com.groomiz.billage.classroom.repository.ClassroomRepository;
import com.groomiz.billage.reservation.entity.Reservation;
import com.groomiz.billage.reservation.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassroomSearchService {

	private final ClassroomRepository classroomRepository;
	private final ReservationRepository reservationRepository;
	private final BuildingRepository buildingRepository;

	public List<ClassroomListResponse> findClassrooms(ClassroomListRequest request) {

		// 건물 정보 조회
		Building building = buildingRepository.findById(request.getBuildingId())
			.orElseThrow(() -> new BuildingException(BuildingErrorCode.BUILDING_NOT_FOUND));

		// 요청된 층이 건물의 층 범위를 벗어나는 경우 예외 발생
		if (request.getFloor() < building.getStartFloor() || request.getFloor() > building.getEndFloor()) {
			throw new BuildingException(BuildingErrorCode.FLOOR_NOT_FOUND);
		}

		List<Classroom> classrooms = classroomRepository.findByCondition(
			request.getBuildingId(),
			request.getFloor(),
			request.getHeadcount()
		);

		// 강의실이 없는 경우 에러 처리
		if (classrooms.isEmpty()) {
			throw new ClassroomException(ClassroomErrorCode.CLASSROOM_NOT_FOUND);
		}

		return classrooms.stream().map(classroom -> {
			// 강의실에 해당하는 예약된 시간 정보 조회
			List<Reservation> reservations = reservationRepository.findByClassroomIdAndApplyDate(classroom.getId(), request.getDate());

			List<ReservationTime> reservationTimes = reservations.stream().map(reservation ->
				new ReservationTime(reservation.getStartTime(), reservation.getEndTime())
			).collect(Collectors.toList());

			ClassroomListResponse response = ClassroomListResponse.builder()
				.classroomId(classroom.getId())
				.classroomName(classroom.getName())
				.classroomNumber(classroom.getNumber())
				.capacity(classroom.getCapacity())
				.reservationTimes(reservationTimes)
				.build();

			return response;

		}).collect(Collectors.toList());
	}

}
