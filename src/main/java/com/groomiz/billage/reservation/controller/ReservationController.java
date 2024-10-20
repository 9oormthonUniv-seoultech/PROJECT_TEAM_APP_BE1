package com.groomiz.billage.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groomiz.billage.auth.document.JwtExceptionDocs;
import com.groomiz.billage.common.dto.StringResponseDto;
import com.groomiz.billage.global.anotation.ApiErrorExceptionsExample;
import com.groomiz.billage.reservation.document.ReservationCancelExceptionDocs;
import com.groomiz.billage.reservation.document.ReservationExceptionDocs;
import com.groomiz.billage.reservation.dto.request.ClassroomReservationRequest;
import com.groomiz.billage.reservation.dto.response.ReservationStatusListResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reservations")
@Slf4j
@Tag(name = "Reservation Controller", description = "[학생] 예약 관련 API")
public class ReservationController {

	@PostMapping
	@Operation(summary = "강의실 예약")
	@ApiErrorExceptionsExample(ReservationExceptionDocs.class)
	public ResponseEntity<StringResponseDto> reserveClassroom(@RequestBody ClassroomReservationRequest request) {

		return ResponseEntity.ok(new StringResponseDto("강의실 예약 요청에 성공하였습니다."));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "강의실 예약 취소")
	@ApiErrorExceptionsExample(ReservationCancelExceptionDocs.class)
	public ResponseEntity<StringResponseDto> cancelClassroomReservation(
		@Parameter(description = "예약 ID", example = "1")
		@PathVariable("id") Long id) {

		return ResponseEntity.ok(new StringResponseDto("강의실 예약 취소에 성공하였습니다."));
	}

	@GetMapping
	@Operation(summary = "예약 현황 목록 조회")
	@ApiErrorExceptionsExample(JwtExceptionDocs.class)
	public ResponseEntity<ReservationStatusListResponse> getAllReservationStatus() {

		ReservationStatusListResponse response = null;
		return ResponseEntity.ok(response);
	}
}
