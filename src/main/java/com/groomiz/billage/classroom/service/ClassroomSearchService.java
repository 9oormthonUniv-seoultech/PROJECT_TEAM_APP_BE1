package com.groomiz.billage.classroom.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.groomiz.billage.classroom.dto.ReservationTime;
import com.groomiz.billage.classroom.dto.request.ClassroomListRequest;
import com.groomiz.billage.classroom.dto.response.ClassroomListResponse;
import com.groomiz.billage.classroom.entity.Classroom;
import com.groomiz.billage.classroom.repository.ClassroomRepository;
import com.groomiz.billage.reservation.entity.Reservation;
import com.groomiz.billage.reservation.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassroomSearchService {

	private final ClassroomRepository classroomRepository;
	private final ReservationRepository reservationRepository;

	public List<ClassroomListResponse> findClassrooms(ClassroomListRequest request) {

		List<Classroom> classrooms = classroomRepository.findByCondition(
			request.getBuildingId(),
			request.getFloor(),
			request.getHeadcount()
		);

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
