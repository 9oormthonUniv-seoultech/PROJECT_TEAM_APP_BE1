package com.groomiz.billage.classroom.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.groomiz.billage.classroom.dto.ReservationTime;
import com.groomiz.billage.classroom.dto.response.ClassroomDetailResponse;
import com.groomiz.billage.classroom.entity.Classroom;
import com.groomiz.billage.classroom.entity.ClassroomImage;
import com.groomiz.billage.classroom.exception.ClassroomErrorCode;
import com.groomiz.billage.classroom.exception.ClassroomException;
import com.groomiz.billage.classroom.repository.ClassroomImageRepository;
import com.groomiz.billage.classroom.repository.ClassroomRepository;
import com.groomiz.billage.reservation.entity.Reservation;
import com.groomiz.billage.reservation.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassroomDetailSearchService {
	private final ClassroomRepository classroomRepository;
	private final ClassroomImageRepository classroomImageRepository;
	private final ReservationRepository reservationRepository;

	public ClassroomDetailResponse findClassroomDetail(Long id, LocalDate date) {

		Classroom classroom = classroomRepository.findById(id)
			.orElseThrow(() -> new ClassroomException(ClassroomErrorCode.CLASSROOM_NOT_FOUND));

		List<String> classroomImages = classroomImageRepository.findByClassroomId(id).stream()
			.map(ClassroomImage::getImageUrl)
			.collect(Collectors.toList());

		List<Reservation> reservations = reservationRepository.findByClassroomIdAndApplyDate(id, date);

		List<ReservationTime> reservationTimes = reservations.stream()
			.map(reservation -> new ReservationTime(reservation.getStartTime(), reservation.getEndTime()))
			.collect(Collectors.toList());

		return ClassroomDetailResponse.builder()
			.classroomId(classroom.getId())
			.classroomName(classroom.getName())
			.classroomNumber(classroom.getNumber())
			.capacity(classroom.getCapacity())
			.description(classroom.getDescription())
			.classroomImage(classroomImages)
			.reservationTimes(reservationTimes)
			.build();
	}
}
