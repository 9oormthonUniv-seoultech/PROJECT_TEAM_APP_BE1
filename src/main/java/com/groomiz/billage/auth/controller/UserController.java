package com.groomiz.billage.auth.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groomiz.billage.auth.document.LoginExceptionDocs;
import com.groomiz.billage.auth.document.RegisterExceptionDocs;
import com.groomiz.billage.auth.document.StudentNumberExcptionDocs;
import com.groomiz.billage.auth.document.VerifyEmailException;
import com.groomiz.billage.auth.dto.LoginRequest;
import com.groomiz.billage.auth.dto.RegisterRequest;
import com.groomiz.billage.auth.service.AuthService;
import com.groomiz.billage.auth.service.UnivcertService;
import com.groomiz.billage.common.dto.SuccessResponse;
import com.groomiz.billage.global.anotation.ApiErrorExceptionsExample;
import com.groomiz.billage.member.exception.MemberErrorCode;
import com.groomiz.billage.member.exception.MemberException;
import com.groomiz.billage.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Controller", description = "회원 인증/인가 관련 API")
public class UserController {

	private final AuthService authService;
	private final UnivcertService univcertService;
	private final MemberService memberService;

	@PostMapping("/login")
	@Operation(summary = "회원 로그인")
	@ApiErrorExceptionsExample(LoginExceptionDocs.class)
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		try {
			authService.login(loginRequest, response);
			return ResponseEntity.ok("Login successful");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
		}
	}

	@PostMapping("/logout")
	@Operation(summary = "회원 로그아웃")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		try {
			authService.logout(request, response);
			return ResponseEntity.ok("Logout successful");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Logout failed: " + e.getMessage());
		}
	}

	@PostMapping("/register")
	@Operation(summary = "회원 가입")
	@ApiErrorExceptionsExample(RegisterExceptionDocs.class)
	public ResponseEntity<?> join(@RequestBody RegisterRequest registerRequest) {

		memberService.register(registerRequest);

		return ResponseEntity.ok("success");
	}

	@GetMapping("/check-student-number")
	@Operation(summary = "학번 중복 확인")
	@ApiErrorExceptionsExample(StudentNumberExcptionDocs.class)
	public ResponseEntity<?> checkStudentNumber(
		@Parameter(description = "학번", example = "20100000") @RequestParam String studentNumber) {

		try {
			authService.checkStudentNumber(studentNumber);
			return ResponseEntity.ok()
				.body(new SuccessResponse(200,"가입 가능한 학번입니다."));
		} catch (MemberException e) {
			if (e.getErrorCode() == MemberErrorCode.INVALID_STUDENT_ID) {
				// 400 응답: 학번이 8자리가 아닌 경우
				return ResponseEntity.badRequest()
					.body(new SuccessResponse(HttpStatus.BAD_REQUEST.value(), "학번은 8자리여야 합니다."));
			} else if (e.getErrorCode() == MemberErrorCode.STUDENT_ID_ALREADY_REGISTERED) {
				// 409 응답: 이미 가입된 학번인 경우
				return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new SuccessResponse(HttpStatus.CONFLICT.value(), "이미 존재하는 학번입니다."));
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new SuccessResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류: " + e.getMessage()));
		}
	}

	@PostMapping("/certificate")
	@Operation(summary = "이메일 인증 요청")
	public ResponseEntity<?> certificate(
		@Parameter(description = "이메일", example = "abc@seoultech.ac.kr") @RequestParam String email,
		@Parameter(description = "학교이름", example = "서울과학기술대학교") @RequestParam String univName,
		@Parameter(description = "재학여부", example = "true") @RequestParam boolean univCheck) {

		Map<?, ?> response = univcertService.certifyEmail(email, univName, univCheck);
		return ResponseEntity.ok(response);
	}


	@PostMapping("/verify")
	@Operation(summary = "이메일 인증 코드 검증")
	@ApiErrorExceptionsExample(VerifyEmailException.class)
	public ResponseEntity<?> verify(
		@Parameter(description = "이메일", example = "abc@mail.hongik.ac.kr") @RequestParam String email,
		@Parameter(description = "학교이름", example = "서울과학기술대학교") @RequestParam String univName,
		@Parameter(description = "인증 코드", example = "3816") @RequestParam int code) {

		Map<?, ?> response = univcertService.verifyEmail(email, univName, code);
		return ResponseEntity.ok(response);

	}


}
