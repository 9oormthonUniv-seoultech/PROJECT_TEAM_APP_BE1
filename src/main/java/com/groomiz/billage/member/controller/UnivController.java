package com.groomiz.billage.member.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groomiz.billage.member.dto.response.AdminListResponse;
import com.groomiz.billage.member.dto.response.CollegeListResponse;
import com.groomiz.billage.member.service.AdminService;
import com.groomiz.billage.member.service.CollegeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/univ")
@Tag(name = "Univ Controller", description = "[학생] 학교 정보 관련 API")
public class UnivController {

	private final CollegeService collegeService;
	private final AdminService adminService;

	@GetMapping("/college")
	@Operation(summary = "단과대/학과 목록 조회")
	public ResponseEntity<List<CollegeListResponse>> findAllCollegeMajor() {
		List<CollegeListResponse> response = collegeService.findAllCollegesMajors();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/admin")
	@Operation(summary = "담당자 목록 조회")
	public ResponseEntity<List<AdminListResponse>> findAllAdmin() {
		List<AdminListResponse> response = adminService.findAllAdminInfo();
		return ResponseEntity.ok(response);
	}
}
