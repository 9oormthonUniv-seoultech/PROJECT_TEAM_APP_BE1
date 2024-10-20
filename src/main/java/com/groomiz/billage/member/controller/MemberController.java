package com.groomiz.billage.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.groomiz.billage.common.dto.SuccessResponse;

import com.groomiz.billage.global.anotation.ApiErrorExceptionsExample;
import com.groomiz.billage.member.document.UserInfoEditExceptionDocs;
import com.groomiz.billage.member.document.UserInfoExceptionDocs;
import com.groomiz.billage.member.document.UserPasswordExceptionDocs;
import com.groomiz.billage.member.dto.request.MemberInfoRequest;
import com.groomiz.billage.member.dto.response.MemberInfoResponse;
import com.groomiz.billage.member.dto.request.PasswordRequest;
import com.groomiz.billage.member.exception.MemberException;
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
	public ResponseEntity<MemberInfoResponse> info() {

		// 현재 사용자 정보 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();

		MemberInfoResponse response = memberService.getMemberInfo(currentUsername);

		return ResponseEntity.ok(response);
	}

	@PutMapping("/info")
	@Operation(summary = "회원 정보 수정")
	@ApiErrorExceptionsExample(UserInfoEditExceptionDocs.class)
	public ResponseEntity<?> updateUserInfo(@RequestBody MemberInfoRequest memberInfoRequest) {

		// 현재 사용자 정보 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();

		try {
			memberService.updateMemberInfo(memberInfoRequest, currentUsername);
		} catch (MemberException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new SuccessResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
		}

		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "회원 정보가 성공적으로 수정되었습니다."));

	}

	@DeleteMapping
	@Operation(summary = "회원 탈퇴")
	@ApiErrorExceptionsExample(UserInfoExceptionDocs.class)
	public ResponseEntity<?> delete() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();

		try {
			memberService.delete(currentUsername);
		} catch (MemberException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new SuccessResponse(HttpStatus.FORBIDDEN.value(), e.getMessage()));
		}

		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "회원 탈퇴가 성공적으로 완료되었습니다."));
	}

	@PutMapping("/password")
	@Operation(summary = "비밀번호 수정")
	@ApiErrorExceptionsExample(UserPasswordExceptionDocs.class)

	public ResponseEntity<?> updatePassword(@RequestBody PasswordRequest passwordRequest) {
		try {
			memberService.updatePassword(passwordRequest);
		}  catch (MemberException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new SuccessResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
		}

		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "비밀번호가 성공적으로 수정되었습니다."));

	}

}
