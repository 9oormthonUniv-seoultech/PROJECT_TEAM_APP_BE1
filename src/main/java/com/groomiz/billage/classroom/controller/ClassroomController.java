package com.groomiz.billage.classroom.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groomiz.billage.classroom.dto.request.ClassroomListRequest;
import com.groomiz.billage.classroom.dto.response.ClassroomDetailResponse;
import com.groomiz.billage.classroom.dto.response.ClassroomListResponse;
import com.groomiz.billage.classroom.service.ClassroomDetailSearchService;
import com.groomiz.billage.classroom.service.ClassroomSearchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/univ/classroom")
@Tag(name = "Classroom Controller", description = "[학생] 강의실 관련 API")
public class ClassroomController {

	private final ClassroomSearchService classroomSearchService;
	private final ClassroomDetailSearchService classroomDetailSearchService;

	@PostMapping
	@Operation(summary = "강의실 목록 조회")
	public ResponseEntity<List<ClassroomListResponse>> findAll(@RequestBody ClassroomListRequest request) {

		List<ClassroomListResponse> response = classroomSearchService.findClassrooms(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/info")
	@Operation(summary = "강의실 상세 조회")
	public ResponseEntity<ClassroomDetailResponse> findByClassroomId(
		@Parameter(description = "강의실 ID", example = "1")
		@RequestParam("id") Long id,
		@RequestParam("date") LocalDate date) {

		ClassroomDetailResponse response = classroomDetailSearchService.findClassroomDetail(id, date);
		return ResponseEntity.ok(response);
	}
}
