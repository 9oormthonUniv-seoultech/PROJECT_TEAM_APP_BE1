package com.groomiz.billage.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groomiz.billage.reservation.dto.request.AdminReservationRequest;
import com.groomiz.billage.reservation.dto.response.AdminReservationResponse;
import com.groomiz.billage.reservation.dto.response.AdminReservationStatusListResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/reservations")
@Tag(name = "Admin Reservation Controller", description = "[관리자] 예약 관련 API")
@RequiredArgsConstructor
public class AdminReservationController {

	@GetMapping
	@Operation(summary = "예약 상태별 리스트 조회")
	public ResponseEntity<List<AdminReservationStatusListResponse>> getReservationsByStatus(
		@RequestParam(required = false) String status) {
		return ResponseEntity.ok(null);
	}

	@PostMapping("/{id}/approve")
	@Operation(summary = "예약 승인")
	public ResponseEntity<Map<String, String>> approveReservation(@PathVariable Long id) {
		Map<String, String> response = new HashMap<>();
		response.put("message", "예약 승인 되었습니다.");
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{id}/reject")
	@Operation(summary = "예약 거절")
	public ResponseEntity<Map<String, String>> rejectReservation(@PathVariable Long id,
		@RequestBody(required = false) String rejectionReason) {
		return ResponseEntity.ok(new AdminReservationReasonResponse("예약 거절 완료되었습니다.", rejectionReason));
	}

	@PostMapping
	@Operation(summary = "예약 요청 처리")
	public ResponseEntity<Map<String, String>> createReservation(
		@RequestBody AdminReservationRequest request) {

		Map<String, String> response = new HashMap<>();
		response.put("message", "예약 완료 하였습니다.");

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	@Operation(summary = "예약 상세 조회")
	public ResponseEntity<AdminReservationResponse> getReservation(@PathVariable Long id) {

		return ResponseEntity.ok(null);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "예약 삭제", description = "단일 예약, 기간 예약, 반복 예약을 삭제합니다.")
	public ResponseEntity<Map<String, String>> deleteReservation(@PathVariable Long id,
		@RequestParam(required = false, defaultValue = "single") String type) {

		Map<String, String> response = new HashMap<>();
		response.put("message", "예약 삭제 하였습니다.");

		return ResponseEntity.ok(response);
	}
}
