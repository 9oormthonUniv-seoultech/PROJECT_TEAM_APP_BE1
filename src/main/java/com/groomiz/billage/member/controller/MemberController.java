package com.groomiz.billage.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groomiz.billage.auth.dto.CustomUserDetails;
import com.groomiz.billage.global.anotation.ApiErrorExceptionsExample;
import com.groomiz.billage.member.document.UserInfoEditExceptionDocs;
import com.groomiz.billage.member.document.UserInfoExceptionDocs;
import com.groomiz.billage.member.document.UserPasswordExceptionDocs;
import com.groomiz.billage.member.dto.request.MemberInfoRequest;
import com.groomiz.billage.member.dto.response.MemberInfoResponse;
import com.groomiz.billage.member.dto.request.PasswordRequest;
import com.groomiz.billage.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "Member Controller", description = "회원 정보 관리 API")
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/info")
	@Operation(summary = "회원 정보 조회")
	@ApiErrorExceptionsExample(UserInfoExceptionDocs.class)
	public ResponseEntity<MemberInfoResponse> info(@AuthenticationPrincipal CustomUserDetails user) {

		MemberInfoResponse response = memberService.getMemberInfo(user.getUsername());

		return ResponseEntity.ok(response);
	}

	@PutMapping("/info")
	@Operation(summary = "회원 정보 수정")
	@ApiErrorExceptionsExample(UserInfoEditExceptionDocs.class)
	public ResponseEntity<String> updatePhoneNumber(@RequestBody MemberInfoRequest memberInfoRequest) {
		return ResponseEntity.ok("success");
	}

	@DeleteMapping
	@Operation(summary = "회원 탈퇴")
	@ApiErrorExceptionsExample(UserInfoExceptionDocs.class)
	public ResponseEntity<String> delete() {
		return ResponseEntity.ok("success");
	}

	@PutMapping("/password")
	@Operation(summary = "비밀번호 수정")
	@ApiErrorExceptionsExample(UserPasswordExceptionDocs.class)
	public ResponseEntity<String> updatePassword(@RequestBody PasswordRequest passwordRequest) {
		return ResponseEntity.ok("success");
	}

}
